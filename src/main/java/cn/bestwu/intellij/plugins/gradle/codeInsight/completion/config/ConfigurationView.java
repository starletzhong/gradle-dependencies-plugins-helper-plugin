package cn.bestwu.intellij.plugins.gradle.codeInsight.completion.config;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.idea.maven.model.MavenRemoteRepository;


public class ConfigurationView {

  private JPanel nexusPanel;
  private JButton resetButton;
  private JCheckBox useNexus;
  private JTextField nexusSearchUrlField;
  private JPanel dpPanel;
  private JPanel mavenIndexPanel;
  private JCheckBox useMavenIndex;
  private JLabel showtip;
  //  private JButton addRemoteRepo;
  private Set<MavenRemoteRepository> remoteRepositories = new HashSet<>();

  public ConfigurationView() {
    useNexus.addActionListener(
        actionEvent -> nexusSearchUrlField.setEnabled(useNexus.isSelected()));
//    useMavenIndex.addActionListener(
//        actionEvent -> addRemoteRepo.setEnabled(useMavenIndex.isSelected()));
//    addRemoteRepo.addActionListener(e -> {
//      final RemoteRepositoryEditor repositoryEditor = new RemoteRepositoryEditor(
//          "Add Maven Remote Repository", "", "", new EditValidator());
//      if (repositoryEditor.showAndGet()) {
//        remoteRepositories.add(new MavenRemoteRepository(repositoryEditor.getName(), null,
//            repositoryEditor.getValue(), null, null, null));
//      }
//    });
  }

  public JPanel getDpPanel() {
    return dpPanel;
  }


  public String getNexusSearchUrlField() {
    return nexusSearchUrlField.getText();
  }

  public void setNexusSearchUrlField(String nexusSearchUrlField) {
    this.nexusSearchUrlField.setText(nexusSearchUrlField);
  }

  public boolean getUseNexus() {
    return useNexus.isSelected();
  }

  public void setUseNexus(boolean selected) {
    useNexus.setSelected(selected);
    nexusSearchUrlField.setEnabled(selected);
  }


  public Set<MavenRemoteRepository> getRemoteRepositories() {
    return remoteRepositories;
  }

  public void setRemoteRepositories(
      Set<MavenRemoteRepository> remoteRepositories) {
    this.remoteRepositories = remoteRepositories;
  }

  public boolean getUseMavenIndex() {
    return useMavenIndex.isSelected();
  }

  public void setUseMavenIndex(boolean selected) {
    useMavenIndex.setSelected(selected);
//    addRemoteRepo.setEnabled(selected);
  }

  public JButton getResetButton() {
    return resetButton;
  }
}