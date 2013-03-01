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
import hudson.model.Action;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * This is a {@link Recorder}, which is a {@link Publisher}.
 * 
 * Publisher are {@link hudson.tasks.BuildStep}s performed in the post build phase. They are designed for reporting.
 * The build phase usually involves building or compiling, and maybe some rudimentary smoke test of your software.
 * Then in the post build phase you're more likely to perform static analysis of your source code, check for coverage and maybe compiler warnings.
 * 
 * Our example demonstrates in a very simple way how the post build step can be used to set the overall result of a given build.
 * 
 * Our example recorder requires the user to enter a string, that must be contained as a part build steps in order to be considered stable.
 * 
 * @author Praqma
 */
public class FirstRecorder extends Recorder {

    /**
     * Required constructor. Our Recorder, which extends Publisher, which implements Describable
     * must have specified a DataBoundConstuctor. The DataBoundConstructor is invoked whenever the job cofiguration 
     * is saved, and is used to bind form input variables to constructor parameters.
     * 
     */
    @DataBoundConstructor
    public FirstRecorder() {
    
    }

      /**
     * Required class with a concrete implementation of the descriptor.
     */
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

         /**
         * Same as with the {@link FirstBuilder}
         * @param project The class name of the selected project type.
         * @return a boolean value indicating whether this BuildStep can be used with the selected Project Type.
         */
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> project) {
            return true;
        }

        /**
         * Required method. Needs to be overridden.
         * 
         * @return the name to be display in the describable list
         */
        @Override
        public String getDisplayName() {
            return "First Recorder";
        }
        
    }

       /**
     * Performs the required operations for this build step. The method should generally return true. If some critical error arises such as
     * not being able to open a required file, it is much better to abort the pipeline by throwing an {@link AbortException}.
     *
     * This very simple reference implementation contains code that checks if the action contains items with the text, you specify when configuring
     * the {@link FirstRecorder} project. 
     * 
     * @param build
     * @param launcher
     * @param listener
     * @return a boolean value indicating proper execution, if true, the next item in build step is picked up for execution
     * @throws InterruptedException
     * @throws IOException 
     */
    @Override
    public boolean perform(Build<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        FirstBuildAction action = build.getAction(FirstBuildAction.class);
        if(action != null) {
            if(!action.hasEvenRandomNumber()) {
                build.setResult(Result.UNSTABLE);
            }
        }
        
        return true;
    }
    
    //Just copy paste this. No reason to worry about this, do it this way and it will work with modern Jenkin installation
    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    /**
     * This method returns a project action you wish to have displayed on the front page
     * @param project
     * @return the project action to be displayed
     */
    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new FirstProjectAction(project);
    }
    
}
