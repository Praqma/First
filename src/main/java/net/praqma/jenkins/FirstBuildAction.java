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

import hudson.model.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Praqma
 */
public class FirstBuildAction implements Action {

    public List<FirstBuildInfo> firstBuildInfo;

    public FirstBuildAction() {    
        firstBuildInfo = new ArrayList<FirstBuildInfo>();
    }
    
    public void addInfo(String javaVersion) {
        firstBuildInfo.add(new FirstBuildInfo(javaVersion));
    }    
    
    @Override
    public String getIconFileName() {
        return "/plugin/first-plugin/images/64x64/one-icon.png";
    }

    @Override
    public String getDisplayName() {
        return "First Build Action";
    }

    @Override
    public String getUrlName() {
        return "firstbuildaction";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(FirstBuildAction.FirstBuildInfo info : firstBuildInfo) {
            builder.append(info + "<br/>");
        }
        return builder.toString();
    }
    
    public static class FirstBuildInfo {
        public String javaVersion;
        public int randomNumber;
        
        public FirstBuildInfo(String javaVersion) {
            this.javaVersion = javaVersion;
            this.randomNumber =  1 + new Random().nextInt(10);
        }

        @Override
        public String toString() {
            return String.format ( "Java version: %s - RandomNumber: %s",javaVersion,randomNumber);
        }        
    }
    
    public boolean hasEvenRandomNumber() {
        for(FirstBuildAction.FirstBuildInfo info : firstBuildInfo) {
            if(info.randomNumber % 2 == 0) {
                return true;
            }
        }
        return false;
    }
}
