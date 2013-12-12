/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import client.gui.model.communication.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class DownloadBatchDialog extends JDialog {
    
    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 100;
    
    private CommunicationNotifier communicationNotifier;
    
    private JComboBox projectList;
    private JButton getSampleButton;
    private JButton cancelButton;
    private JButton downloadButton;
    
    private SampleViewDialog sampleViewDialog;
    
    private ArrayList<String> projectNames;
    private ArrayList<Integer> projectIds;
    private ArrayList<String> sampleImages;
    
    private int selectedIndex;
    private Integer selectedId;
    
    public DownloadBatchDialog(JFrame parent, CommunicationLinker communicationLinker) {
        
        super(parent, "Download Batch", true);
        
        this.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        this.setLocation(Client.SCREEN_WIDTH / 2 - DIALOG_WIDTH / 2,
                         Client.SCREEN_HEIGHT / 2 - DIALOG_HEIGHT / 2);
        
        this.pack();
        this.setResizable(false);
        
        communicationNotifier = communicationLinker.getCommunicationNotifier();
        communicationLinker.subscribe(communicationSubscriber);
        
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        
        selectedIndex = 0;
        selectedId = 0;
        
        createComponents();
        
    }
    
    private void createComponents() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(275, 70));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        Dimension innerBuffer = new Dimension(5, 0);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JLabel projectLabel = new JLabel("Project:");
        topPanel.add(projectLabel);
        topPanel.add(Box.createRigidArea(innerBuffer));
        projectList = createProjectList();
        topPanel.add(projectList);
        topPanel.add(Box.createRigidArea(innerBuffer));
        getSampleButton = new JButton("View Sample");
        getSampleButton.addActionListener(getSampleButtonListener);
        topPanel.add(getSampleButton);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(cancelAction);
        bottomPanel.add(cancelButton);
        bottomPanel.add(Box.createRigidArea(innerBuffer));
        downloadButton = new JButton("Download");
        downloadButton.addActionListener(downloadAction);
        bottomPanel.add(downloadButton);
        
        mainPanel.add(bottomPanel);
        
        this.add(mainPanel);
        
    }
    
    private void reset() {
        
        projectList.setModel(new DefaultComboBoxModel(projectNames.toArray()));
        projectList.setSelectedIndex(0);
        
    }
    
    private JComboBox createProjectList() {
        
        projectList = new JComboBox(new String[]{"blah", "boo"});
        projectList.addActionListener(projectSelectListener);
        return projectList;
        
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber =
                                        new AbstractCommunicationSubscriber() {

        private ValidateUser_Result result;
                                            
        @Override
        public void login(ValidateUser_Result result) {
            this.result = result;
            if (result != null && result.validated()) {
                worker.execute();
            }
        }
        
        /**
         * Get projects in background, because otherwise it produces lag
         * loading the GUI.
         */
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                GetProjects_Result projectResult = communicationNotifier.getProjects();
                if (projectResult != null) {
                    projectNames = (ArrayList) projectResult.getProjectNames();
                    projectIds = (ArrayList) projectResult.getProjectIds();
                    reset();
                    
                    // Get all images
                    sampleImages = new ArrayList<>();
                    for (Integer ID : projectIds) {
                        int id = ID.intValue();
                        GetSampleImage_Result result = communicationNotifier.getSampleImage(id);
                        if (result != null) {
                            sampleImages.add(result.imagePath());
                        }
                    }
                }
                return null;
            }
            
        };

    };
    
    private ActionListener projectSelectListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedIndex = projectList.getSelectedIndex();
            if (selectedIndex >= 0) {
                selectedId = projectIds.get(selectedIndex);
            }
        }
        
    };
    
    private ActionListener getSampleButtonListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            int validNumber = sampleImages.size();
            if (selectedIndex < validNumber) {
                String path = sampleImages.get(projectList.getSelectedIndex());
                BufferedImage sample = communicationNotifier.downloadImage(sampleImages.get(selectedIndex));
                String name = "Sample image from " + projectNames.get(selectedIndex);
                sampleViewDialog = new SampleViewDialog(DownloadBatchDialog.this,
                                                            name, sample);
                sampleViewDialog.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(DownloadBatchDialog.this,
                                              "This project is already fully indexed!",
                                              "Hooray!", JOptionPane.INFORMATION_MESSAGE);
            }
            
        }
        
    };
    
    private ActionListener cancelAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
        
    };

    private ActionListener downloadAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            communicationNotifier.downloadBatch(selectedId);
            dispose();
        }
        
    };

}
