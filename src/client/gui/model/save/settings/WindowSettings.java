/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save.settings;

import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author schuyler
 */
public class WindowSettings implements DataModel {
    
    private Dimension windowSize;
    private Point windowLocation;
    
    // Location of the divider that divides top from bottom
    private int horizontalDividerLocation;
    
    // Location of the divider that divides left from right
    private int verticalDividerLocation;
    private int selectedEntryTab;
    private int selectedExtrasTab;
    
    public WindowSettings() {
        set(null, null, 0, 0, new EntryTab.None(), new EntryTab.None());
    }
    
    public WindowSettings(Dimension windowSize, Point windowLocation) {
        set(windowSize, windowLocation, 0, 0, new EntryTab.None(), new EntryTab.None());
    }
    
    public WindowSettings(int horizontalDividerLocation) {
        set(null, null, horizontalDividerLocation, 0, new EntryTab.None(), new EntryTab.None());
    }
    
    public WindowSettings(EntryTab selectedEntryTab, EntryTab selectedExtrasTab) {
        set(null, null, 0, 0, selectedEntryTab, selectedExtrasTab);
    }
    
    public WindowSettings(int verticalDividerLocation,
                          EntryTab selectedEntryTab, EntryTab selectedExtrasTab) {
        
        set(null, null, 0, verticalDividerLocation, selectedEntryTab, selectedExtrasTab);
        
    }
    
    private void set(Dimension windowSize, Point windowLocation,
                     int horizontalDividerLocation, int verticalDividerLocation,
                     EntryTab selectedEntryTab, EntryTab selectedExtrasTab) {
        
        this.windowSize = windowSize;
        this.windowLocation = windowLocation;
        this.horizontalDividerLocation = horizontalDividerLocation;
        this.verticalDividerLocation = verticalDividerLocation;
        this.selectedEntryTab = selectedEntryTab.tabNumber;
        this.selectedExtrasTab = selectedExtrasTab.tabNumber;
        
    }
    
    public Dimension windowSize() {
        return windowSize;
    }
    
    public Point windowLocation() {
        return windowLocation;
    }
    
    public int horizontalDividerLocation() {
        return horizontalDividerLocation;
    }
                    
    public int verticalDividerLocation() {
        return verticalDividerLocation;
    }
        
    public int selectedEntryTab() {
        return selectedEntryTab;
    }

    public int selectedExtrasTab() {
        return selectedExtrasTab;
    }
    
    /**
     * Combine other window settings with this one.
     * 
     * Uses window settings for this first if initialized, otherwise it uses
     * the settings for otherSettings.
     * 
     * @param otherSettings the WindowSettings to combine with this.
     */
    public void combine(WindowSettings otherSettings) {
        
        windowSize = this.windowSize == null ?
                            otherSettings.windowSize : this.windowSize;
        windowLocation = this.windowLocation == null ?
                            otherSettings.windowLocation : this.windowLocation;
        horizontalDividerLocation = this.horizontalDividerLocation == 0 ?
                            otherSettings.horizontalDividerLocation : this.horizontalDividerLocation;
        verticalDividerLocation = this.verticalDividerLocation == 0 ?
                            otherSettings.verticalDividerLocation : this.verticalDividerLocation;
        selectedEntryTab = this.selectedEntryTab == -1 ?
                            otherSettings.selectedEntryTab : this.selectedEntryTab;
        selectedExtrasTab = this.selectedExtrasTab == -1 ?
                            otherSettings.selectedExtrasTab : this.selectedExtrasTab;
        
    }
    
    public static class EntryTab {
        
        private static final int NONE = -1;
        private static final int FIRST = 0;
        private static final int SECOND = 1;
        
        public int tabNumber;
        
        public EntryTab(int tabNumber) {
            if (tabNumber < NONE || tabNumber > SECOND) {
                throw new IllegalArgumentException();
            }
            this.tabNumber = tabNumber;
        }
        
        public static class None extends EntryTab {
            public None() {
                super(NONE);
            }
        }
        
        public static class First extends EntryTab {
            public First() {
                super(FIRST);
            }
        }
        
        public static class Second extends EntryTab {
            public Second() {
                super(SECOND);
            }
        }
        
    }
    
}
