package me.t3sl4.hydraulic.controllers.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.HTTP.HTTPMethod;
import me.t3sl4.hydraulic.utils.service.UserDataService.Profile;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;

public class ProfileEditController {
    @FXML
    private Label btn_exit;

    @FXML
    private ImageView profilePhotoImageView;

    @FXML
    private TextField isimSoyisimText;
    @FXML
    private TextField ePostaText;
    @FXML
    private TextField telefonText;
    @FXML
    private TextField kullaniciAdiText;
    @FXML
    private TextField sifreText;
    @FXML
    private TextField sirketText;

    @FXML
    private ImageView onderLogo;

    @FXML
    private Circle secilenFoto;

    @FXML
    private Button togglePasswordButton;
    @FXML
    private TextField sifrePassword;
    @FXML
    private ImageView passwordVisibilityIcon;

    String secilenPhotoPath = null;
    private String girilenSifre = "";

    @FXML
    public void initialize() {
        initializeUser();
        Profile.downloadAndSetProfilePhoto(SystemVariables.loggedInUser.getUsername(), secilenFoto, profilePhotoImageView);
        togglePasswordButton.setOnMouseClicked(event -> togglePasswordVisibility());
        sifreText.textProperty().addListener((observable, oldValue, newValue) -> {
            girilenSifre = newValue;
        });
    }

    private void togglePasswordVisibility() {
        if (sifreText.isVisible()) {
            sifreText.setManaged(false);
            sifreText.setVisible(false);
            sifrePassword.setManaged(true);
            sifrePassword.setVisible(true);
            sifrePassword.setText(girilenSifre);
            passwordVisibilityIcon.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/icons/ikon_hide_pass.png"))));
        } else {
            sifreText.setManaged(true);
            sifreText.setVisible(true);
            sifrePassword.setManaged(false);
            sifrePassword.setVisible(false);
            passwordVisibilityIcon.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/icons/ikon_show_pass.png"))));
        }
    }

    @FXML
    public void kayitBilgiTemizle() {
        secilenFoto.setVisible(false);
        isimSoyisimText.clear();
        ePostaText.clear();
        telefonText.clear();
        kullaniciAdiText.clear();
        sifreText.clear();
        sirketText.clear();
    }

    @FXML
    private void uploadProfilePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Resim Dosyaları", "*.jpg")
        );

        Stage stage = (Stage) profilePhotoImageView.getScene().getWindow();
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            secilenFoto.setFill(new ImagePattern(image));
            secilenFoto.setVisible(true);
            profilePhotoImageView.setImage(image);
            profilePhotoImageView.setVisible(false);
            secilenPhotoPath = selectedFile.getAbsolutePath();
        }
    }

    @FXML
    private void profilGuncelleme() {
        Stage stage = (Stage) kullaniciAdiText.getScene().getWindow();
        String userName = kullaniciAdiText.getText();
        String password = sifreText.getText();
        String nameSurname = isimSoyisimText.getText();
        String eMail = ePostaText.getText();
        String companyName = sirketText.getText();
        String phone = telefonText.getText();
        String profilePhotoPath = userName + ".jpg";

        if (password.isEmpty()) {
            password = null;
        }
        String registerJsonBody;

        if (password != null) {
            registerJsonBody =
                    "{" +
                            "\"userID\": \"" + SystemVariables.loggedInUser.getUserID() + "\"," +
                            "\"userData\": {" +
                            "  \"userName\": \"" + userName + "\"," +
                            "  \"nameSurname\": \"" + nameSurname + "\"," +
                            "  \"eMail\": \"" + eMail + "\"," +
                            "  \"phoneNumber\": \"" + phone + "\"," +
                            "  \"companyName\": \"" + companyName + "\"," +
                            "  \"password\": \"" + password + "\"" +
                            "}" +
                            "}";
        } else {
            registerJsonBody =
                    "{" +
                            "\"userID\": \"" + SystemVariables.loggedInUser.getUserID() + "\"," +
                            "\"userData\": {" +
                            "  \"userName\": \"" + userName + "\"," +
                            "  \"nameSurname\": \"" + nameSurname + "\"," +
                            "  \"eMail\": \"" + eMail + "\"," +
                            "  \"phoneNumber\": \"" + phone + "\"," +
                            "  \"companyName\": \"" + companyName + "\"" +
                            "}" +
                            "}";
        }

        sendUpdateRequest(registerJsonBody, userName, stage);
    }

    private void sendUpdateRequest(String jsonBody, String username, Stage stage) {
        String registerUrl = BASE_URL + updateProfileURLPrefix;
        HTTPMethod.sendAuthorizedJsonRequest(registerUrl, "POST", jsonBody, SystemVariables.loggedInUser.getAccessToken(), new HTTPMethod.RequestCallback() {
            @Override
            public void onSuccess(String response) throws IOException {
                if(secilenPhotoPath != null) {
                    deleteOldPhoto(username);
                    uploadProfilePhoto2Server(stage);
                }
                Utils.showSuccessMessage("Profilin başarılı bir şekilde güncellendi !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
                JSONObject userCred = new JSONObject(jsonBody);
                JSONObject dataCred = userCred.getJSONObject("userData");

                loggedInUser.setUsername(dataCred.getString("userName"));
                loggedInUser.setFullName(dataCred.getString("nameSurname"));
                loggedInUser.setEmail(dataCred.getString("eMail"));
                loggedInUser.setPhone(dataCred.getString("phoneNumber"));
                loggedInUser.setCompanyName(dataCred.getString("companyName"));
                refreshScreen();
            }

            @Override
            public void onFailure() {
                System.out.println(registerUrl + jsonBody);
                Utils.showErrorMessage("Profil güncellenirken hata meydana geldi !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
            }
        });
    }

    private void uploadProfilePhoto2Server(Stage stage) throws IOException {
        String uploadUrl = BASE_URL + uploadProfilePhotoURLPrefix;
        String username = kullaniciAdiText.getText();

        File profilePhotoFile = new File(secilenPhotoPath);
        if (!profilePhotoFile.exists()) {
            Utils.showErrorMessage("Profil fotoğrafı bulunamadı!", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
            return;
        }

        HTTPMethod.uploadFile(uploadUrl, "POST", profilePhotoFile, username, new HTTPMethod.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Utils.showSuccessMessage("Profil fotoğrafı güncellendi!", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
            }

            @Override
            public void onFailure() {
                Utils.showErrorMessage("Profil fotoğrafı yüklenirken hata meydana geldi!", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
            }
        });
    }

    private void deleteOldPhoto(String username) {
        String dosyaYolu = profilePhotoLocalPath + username + ".jpg";

        File dosya = new File(dosyaYolu);

        if (dosya.exists()) {
            if (dosya.delete()) {
                System.out.println("Dosya başarıyla silindi.");
            } else {
                System.out.println("Dosya silinemedi.");
            }
        } else {
            System.out.println("Dosya mevcut değil.");
        }
    }

    private void refreshScreen() {
        initializeUser();
        Profile.downloadAndSetProfilePhoto(SystemVariables.loggedInUser.getUsername(), secilenFoto, profilePhotoImageView);
        sifreText.clear();
    }

    private void initializeUser() {
        isimSoyisimText.setText(loggedInUser.getFullName());
        kullaniciAdiText.setText(loggedInUser.getUsername());
        ePostaText.setText(loggedInUser.getEmail());
        telefonText.setText(loggedInUser.getPhone());
        sirketText.setText(loggedInUser.getCompanyName());
    }
}
