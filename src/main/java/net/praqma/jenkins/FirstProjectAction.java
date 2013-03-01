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
 * Class implementing actionable and ProminentProjectAction.
 * 
 * Prominent project actions means that the plugin will have a footprint on the jobs main page.
 * It also allows us to have a menu item.
 * 
 * Opposite the build action(or just {@link Action}), the project action is NOT persisted.
 * 
 * @author Praqma
 */
public class FirstProjectAction implements ProminentProjectAction{

    public final AbstractProject<?,?> project;
    
    /**
     * If this method returns null, no icon will be used and the link will not be visible
     */ 
    @Override
    public String getIconFileName() {
        return "/plugin/first-plugin/images/64x64/one-icon.png";
    }

    /**
     * This method is used to create the text for the link to 'Project Action' link on the Job's
     * front page.
     * 
     * @return the text to be displayed on the project action link page 
     */
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
    
    /**
     * 
     * @return the last build action associated with this project. 
     */
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
