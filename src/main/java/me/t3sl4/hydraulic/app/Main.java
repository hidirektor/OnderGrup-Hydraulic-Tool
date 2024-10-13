package me.t3sl4.hydraulic.app;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;

import java.util.List;
import java.util.prefs.Preferences;

public class Main extends Application {
    public static User loggedInUser;

    List<Screen> screens = Screen.getScreens();

    public static Screen defaultScreen;

    @lombok.SneakyThrows
    @Override
    public void start(Stage primaryStage) {
        Utils.prefs = Preferences.userRoot().node(this.getClass().getName());
        String defaultMonitor = Utils.checkDefaultMonitor();

        if (defaultMonitor == null) {
            if (screens.size() > 1) {
                Utils.showMonitorSelectionScreen(screens, null, true);
            } else {
                SceneUtil.openMainScreen(screens.get(0));
            }
        } else {
            int monitorIndex = Integer.parseInt(defaultMonitor.split(" ")[1]) - 1;
            defaultScreen = screens.get(monitorIndex);
            SceneUtil.openMainScreen(screens.get(monitorIndex));
        }

        Thread systemThread = new Thread(FileUtil::setupFileSystem);
        systemThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
