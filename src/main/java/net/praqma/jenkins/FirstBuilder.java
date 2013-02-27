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
import hudson.model.BuildListener;
import hudson.model.Project;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.praqma.util.execute.CommandLine;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author Praqma
 */
public class FirstBuilder extends Builder {
    
    public final boolean runOnSlaves;
    
    @Extension
    public static class FirstBuilderImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> proj) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "First Builder";
        }        
    }
    
    @DataBoundConstructor
    public FirstBuilder(final boolean runOnSlaves) { 
        this.runOnSlaves = runOnSlaves;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("My First Builder");
        
        String javaVersion = null;
        
        if ( runOnSlaves) {
            javaVersion = build.getWorkspace().act(new FirstRemoteOperation());
        } else {
            javaVersion = new FirstRemoteOperation().invoke(null, null);
        }

        listener.getLogger().println( "Found this java version: " + javaVersion);
        
        FirstBuildAction action = build.getAction(FirstBuildAction.class);
        
        
        if(action != null) {
            action.addInfo(javaVersion);
        } else {
            action = new FirstBuildAction();
            action.addInfo(javaVersion);
            build.addAction(action);        
        }
        
        return true;
    }
}
