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

import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.praqma.util.execute.CommandLine;

/**
 * Class representing a remote file operation. Whenever Jenkins acts() upon it's workspace 
 * the method invoke() is called on the machine executing the job (Either the remote or the master).
 *
 * The class can have a constructor with parameters. These parameters are serialized and available
 * on the remote.
 * 
 * @author praqma
 * 
 */
public class FirstRemoteOperation implements FilePath.FileCallable<String>  {
    
    /**
     * This class implements {@link FilePath.FileCallable}. The invoke method is executed either on the master or the remote.
     * The returned value MUST be {@link Serializable} if the task is ever going be executed remotely.
     * 
     * @param f the remote workspace (or null if not remote)
     * @param channel the remote channel (or null if not remote)
     * @return a String containing information about the installed java version
     * @throws IOException
     * @throws InterruptedException 
     */
    @Override
    public String invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
        String javaVersion = "Unknown";
        //Read command line output into a list of strings.
        List<String> standardOut = CommandLine.getInstance().run(" java -version" ).stdoutList;
        if(standardOut.size() > 0) {
            javaVersion = standardOut.get(0);
        }
        return javaVersion;
    }
    
}
