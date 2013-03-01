/*
 * The MIT License
 *
 * Copyright 2013 Praqma.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.praqma.jenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * The Builder for our project. We extend from Builder which implements BuildStep,
 * which has a perform method we will use. Additionally all {@link hudson.tasks.BuildStep}s have a prebuild method to
 * override, which runs before the build step.
 *
 * The main purpose of this example builder is to extract environment information from the executing slaves, and make 
 * this data available for the view we wish to present it in.
 * 
 * During execution we re-use already added actions, and add the discovered data to the already existing build action.
 * 
 * @author Praqma
 */
public class FirstBuilder extends Builder {
    
    public final boolean runOnSlaves;
    
    /**
     * Required static constructor. This is used to create 'One Project Builder' BuildStep in the list-box item on your jobs
     * configuration page. 
     */
    @Extension
    public static class FirstBuilderImpl extends BuildStepDescriptor<Builder> {

        /**
         * This is used to determine if this build step is applicable for your chosen projec type. (FreeStyle, MultiConfiguration, Maven) 
         * Some plugin build steps might be made to be only available to MultiConfiguration projects.
         * 
         * Required. In our example we require the project to be a free-style project.
         * 
         * @param proj The current project
         * @return a boolean indicating whether this build step can be chose given the project type
         */
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> proj) {
            return true;
        }
        /**
         * Required method.
         * 
         * @return The text to be displayed when selecting your BuildStep, in the project
         */
        @Override
        public String getDisplayName() {
            return "First Builder";
        }        
    }
    
    @DataBoundConstructor
    public FirstBuilder(final boolean runOnSlaves) { 
        this.runOnSlaves = runOnSlaves;
    }

    /**
     * Override this method to get your operation done in the build step. When invoked, it is up to you, as a plugin developer
     * to add your actions, and/or perform the operations required by your plugin in this build step. Equally, it is up
     * to the developer to make the code run on the slave(master or an actual remote). This must be done given the builds
     * workspace, as in build.getWorkspace(). The workspace is the link to the slave, as it is the representation of the
     * remotes file system.
     *
     * Build steps as you add them to your job configuration are executed sequentially, and the return value for your
     * builder should indicate whether to execute the next build step in the list.
     *
     * @param build the current build
     * @param launcher the current launcher
     * @param listener the build listener
     * @return a boolean indicating wheather to proceed with the next buildstep
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        //Print to the console
        listener.getLogger().println("My First Builder");
        
        String javaVersion = null;
        
        //Value [runOnSlaves] from build step configuration.
        if ( runOnSlaves) {
            //Tell jenkins to act upon the current workspace (Can be remote, or local)
            javaVersion = build.getWorkspace().act(new FirstRemoteOperation());
        } else {
            //Else, force this to be performed on master, regardless.
            javaVersion = new FirstRemoteOperation().invoke(null, null);
        }

        listener.getLogger().println( "Found this java version: " + javaVersion);
        
        //Extract our action from the build, null if no action found.
        FirstBuildAction action = build.getAction(FirstBuildAction.class);
        
        /**
         * This is where we add our build action to the build.
         * We only do this if we haven't previously added one.
         */
        if(action != null) {
            action.addInfo(javaVersion);
        } else {
            //No action yet...let's add one
            action = new FirstBuildAction();
            action.addInfo(javaVersion);
            build.addAction(action);        
        }
        
        return true;
    }
}
