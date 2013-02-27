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

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;

/**
 *
 * @author Praqma
 */
public class FirstProjectAction implements ProminentProjectAction{

    public final AbstractProject<?,?> project;
    
    @Override
    public String getIconFileName() {
        return "/plugin/first-plugin/images/64x64/one-icon.png";
    }

    @Override
    public String getDisplayName() {
        return "First Project Action";
    }

    @Override
    public String getUrlName() {
        return "firstprojectaction";
    }

    public FirstProjectAction(AbstractProject<?,?> project) {
        this.project = project;
    }
    
    public FirstBuildAction getLastBuildAction() {
        for( AbstractBuild<?, ?> b = project.getLastCompletedBuild() ; b != null ; b = b.getPreviousBuild() ) {
            FirstBuildAction action = b.getAction( FirstBuildAction.class );
            if( action != null ) {
                return action;
            }
        }

        return null;
    }
  
}
