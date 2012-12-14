/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.resolver.util;

/**
 *
 * @author Jesper Terkelsen
 */
public class PosixFileModeParser {

    private final int mode;
    private static final int OTHER_EXECUTE = 1;
    private static final int OTHER_WRITE = 2;
    private static final int OTHER_READ = 4;

    private static final int GROUP_EXECUTE = 8;
    private static final int GROUP_WRITE = 16;
    private static final int GROUP_READ = 32;
    
    private static final int USER_EXECUTE = 64;
    private static final int USER_WRITE = 128;
    private static final int USER_READ = 256;
    
    public PosixFileModeParser(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
    
    public boolean isOtherExecute(){
        return ((mode & OTHER_EXECUTE) != 0);
    }
    
    public boolean isOtherWrite(){
        return ((mode & OTHER_WRITE) != 0);
    }
    
    public boolean isOtherRead(){
        return ((mode & OTHER_READ) != 0);
    }    
        
    public boolean isGroupExecute(){
        return ((mode & GROUP_EXECUTE) != 0);
    }
    
    public boolean isGroupWrite(){
        return ((mode & GROUP_WRITE) != 0);
    }
    
    public boolean isGroupRead(){
        return ((mode & GROUP_READ) != 0);
    }    
            
        
    public boolean isUserExecute(){
        return ((mode & USER_EXECUTE) != 0);
    }
    
    public boolean isUserWrite(){
        return ((mode & USER_WRITE) != 0);
    }
    
    public boolean isUserRead(){
        return ((mode & USER_READ) != 0);
    }    
            
    
    public String getPrintedPermissions(){
        StringBuilder result = new StringBuilder();
        result.append(isUserRead() ? "r" : "-");
        result.append(isUserWrite() ? "w" : "-");
        result.append(isUserExecute()? "x" : "-");
        result.append(isGroupRead() ? "r" : "-");
        result.append(isGroupWrite() ? "w" : "-");
        result.append(isGroupExecute()? "x" : "-");
        result.append(isOtherRead() ? "r" : "-");
        result.append(isOtherWrite() ? "w" : "-");
        result.append(isOtherExecute()? "x" : "-");        
        return result.toString();
    }
    
}
