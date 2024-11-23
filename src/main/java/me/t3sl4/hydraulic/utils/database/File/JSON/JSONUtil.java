package me.t3sl4.hydraulic.utils.database.File.JSON;

import me.t3sl4.hydraulic.utils.database.Model.HydraulicData.HydraulicData;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONUtil {
    private static final Logger logger = Logger.getLogger(JSONUtil.class.getName());

    public static void loadJSONData() {
        readJson4DefinedTanksClassic(SystemVariables.cabinsDBPath, SystemVariables.getLocalHydraulicData());
        readJson4DefinedTanksPowerPack(SystemVariables.cabinsDBPath, SystemVariables.getLocalHydraulicData());
        readJson4Bosluk(SystemVariables.generalDBPath, SystemVariables.getLocalHydraulicData());
        readJson4UniteType(SystemVariables.generalDBPath, SystemVariables.getLocalHydraulicData());
    }
    private static void readJson4DefinedTanksClassic(String filePath, HydraulicData hydraulicData) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray classicCabinetes = jsonObject.getJSONArray("classic_cabins");

            for (int j = 0; j < classicCabinetes.length(); j++) {
                JSONObject cabinet = classicCabinetes.getJSONObject(j);

                String tankName = cabinet.getString("tankName");
                String kabinName = cabinet.getString("kabinName");
                int kabinHacim = cabinet.getInt("kabinHacim");
                int kabinX = cabinet.getInt("kabinX (G)");
                int kabinY = cabinet.getInt("kabinY (D)");
                int kabinH = cabinet.getInt("kabinH (Y)");
                int tankX = cabinet.getInt("tankX (G)");
                int tankY = cabinet.getInt("tankY (D)");
                int tankH = cabinet.getInt("tankH (Y)");
                String kabinKodu = cabinet.getString("kabinKodu");
                String yagTankiKodu = cabinet.getString("yagTankiKodu");
                String malzemeAdi = cabinet.getString("malzemeAdi");

                Kabin tank = new Kabin(tankName, kabinName, kabinHacim, kabinX, kabinY, kabinH, tankX, tankY, tankH, kabinKodu, yagTankiKodu, malzemeAdi);
                hydraulicData.inputTanks.add(tank);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readJson4DefinedTanksPowerPack(String filePath, HydraulicData hydraulicData) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray classicCabinetes = jsonObject.getJSONArray("power_pack_cabins");

            for (int j = 0; j < classicCabinetes.length(); j++) {
                JSONObject cabinet = classicCabinetes.getJSONObject(j);

                String tankName = cabinet.getString("tankName");
                String kabinName = cabinet.getString("kabinName");
                int kabinHacim = cabinet.getInt("kabinHacim");
                int kabinX = cabinet.getInt("kabinX (G)");
                int kabinY = cabinet.getInt("kabinY (D)");
                int kabinH = cabinet.getInt("kabinH (Y)");
                int tankX = cabinet.getInt("tankX (G)");
                int tankY = cabinet.getInt("tankY (D)");
                int tankH = cabinet.getInt("tankH (Y)");
                String kabinKodu = cabinet.getString("kabinKodu");
                String yagTankiKodu = cabinet.getString("yagTankiKodu");
                String malzemeAdi = cabinet.getString("malzemeAdi");

                Kabin tank = new Kabin(tankName, kabinName, kabinHacim, kabinX, kabinY, kabinH, tankX, tankY, tankH, kabinKodu, yagTankiKodu, malzemeAdi);
                hydraulicData.inputTanks.add(tank);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readJson4Bosluk(String filePath, HydraulicData hydraulicData) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject voidValues = jsonObject.getJSONObject("void_values");

            hydraulicData.kampanaTankArasiBoslukX = Integer.parseInt(voidValues.getString("kampanaTankArasiBoslukX"));
            hydraulicData.kampanaTankArasiBoslukY = Integer.parseInt(voidValues.getString("kampanaTankArasiBoslukY"));
            hydraulicData.kampanaBoslukYOn = Integer.parseInt(voidValues.getString("kampanaBoslukYOn"));
            hydraulicData.tekHizTankArasiBoslukX = Integer.parseInt(voidValues.getString("tekHizTankArasiBoslukX"));
            hydraulicData.tekHizTankArasiBoslukY = Integer.parseInt(voidValues.getString("tekHizTankArasiBoslukY"));
            hydraulicData.tekHizAraBoslukX = Integer.parseInt(voidValues.getString("tekHizAraBoslukX"));
            hydraulicData.tekHizYOn = Integer.parseInt(voidValues.getString("tekHizYOn"));
            hydraulicData.tekHizBlokX = Integer.parseInt(voidValues.getString("tekHizBlokX"));
            hydraulicData.tekHizBlokY = Integer.parseInt(voidValues.getString("tekHizBlokY"));
            hydraulicData.ciftHizTankArasiBoslukX = Integer.parseInt(voidValues.getString("ciftHizTankArasiBoslukX"));
            hydraulicData.ciftHizTankArasiBoslukY = Integer.parseInt(voidValues.getString("ciftHizTankArasiBoslukY"));
            hydraulicData.ciftHizAraBoslukX = Integer.parseInt(voidValues.getString("ciftHizAraBoslukX"));
            hydraulicData.ciftHizYOn = Integer.parseInt(voidValues.getString("ciftHizYOn"));
            hydraulicData.ciftHizBlokX = Integer.parseInt(voidValues.getString("ciftHizBlokX"));
            hydraulicData.ciftHizBlokY = Integer.parseInt(voidValues.getString("ciftHizBlokY"));
            hydraulicData.kilitliBlokTankArasiBoslukX = Integer.parseInt(voidValues.getString("kilitliBlokTankArasiBoslukX"));
            hydraulicData.kilitliBlokTankArasiBoslukY = Integer.parseInt(voidValues.getString("kilitliBlokTankArasiBoslukY"));
            hydraulicData.kilitliBlokAraBoslukX = Integer.parseInt(voidValues.getString("kilitliBlokAraBoslukX"));
            hydraulicData.kilitliBlokYOn = Integer.parseInt(voidValues.getString("kilitliBlokYOn"));
            hydraulicData.kilitliBlokX = Integer.parseInt(voidValues.getString("kilitliBlokX"));
            hydraulicData.kilitliBlokY = Integer.parseInt(voidValues.getString("kilitliBlokY"));
            hydraulicData.kilitMotorTankArasiBoslukX = Integer.parseInt(voidValues.getString("kilitMotorTankArasiBoslukX"));
            hydraulicData.kilitMotorTankArasiBoslukY = Integer.parseInt(voidValues.getString("kilitMotorTankArasiBoslukY"));
            hydraulicData.kilitMotorAraBoslukX = Integer.parseInt(voidValues.getString("kilitMotorAraBoslukX"));
            hydraulicData.kilitMotorYOn = Integer.parseInt(voidValues.getString("kilitMotorYOn"));
            hydraulicData.tekHizKilitAyriY = Integer.parseInt(voidValues.getString("tekHizKilitAyriY"));
            hydraulicData.tekHizKilitAyriYOn = Integer.parseInt(voidValues.getString("tekHizKilitAyriYOn"));
            hydraulicData.ciftHizKilitAyriY = Integer.parseInt(voidValues.getString("ciftHizKilitAyriY"));
            hydraulicData.ciftHizKilitAyriYOn = Integer.parseInt(voidValues.getString("ciftHizKilitAyriYOn"));
            hydraulicData.kilitMotorX = Integer.parseInt(voidValues.getString("kilitMotorX"));
            hydraulicData.kilitMotorY = Integer.parseInt(voidValues.getString("kilitMotorY"));
            hydraulicData.kayipLitre = Integer.parseInt(voidValues.getString("kayipLitre"));
            hydraulicData.defaultHeight = Integer.parseInt(voidValues.getString("defaultHeight"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readJson4UniteType(String filePath, HydraulicData hydraulicData) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject unitTypeValues = jsonObject.getJSONObject("unit_types");

            hydraulicData.uniteTipiDegerleri.add(unitTypeValues.getString("classic"));
            hydraulicData.uniteTipiDegerleri.add(unitTypeValues.getString("power_pack"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
