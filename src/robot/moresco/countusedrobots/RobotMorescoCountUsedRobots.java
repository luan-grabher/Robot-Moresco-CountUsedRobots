package robot.moresco.countusedrobots;

import Entity.Executavel;
import Robo.AppRobo;
import fileManager.FileManager;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import org.ini4j.Ini;

public class RobotMorescoCountUsedRobots {

    public static String testParameters = "";
    public static String nomeApp = "";
    public static Integer month = 0;
    public static Integer year = 2021;
    public static Calendar monthCal = Calendar.getInstance();    
    public static Ini ini;

    public static void main(String[] args) {

        try {
            ini = new Ini(fileManager.FileManager.getFile("config.ini"));

            nomeApp = "Utilização dos robôs mensal ";

            AppRobo robo = new AppRobo(nomeApp);
            robo.definirParametros();

            if (args.length > 0 && args[0].equals("test")) {
                robo.definirParametros(testParameters);
            }

            
            //Por padrao define como o mês anterior ao atual
            monthCal.add(Calendar.MONTH, -1);
            
            //Se o mes for null, pega o mes do calendario, se não pega o mes dos parametros e diminui 1 para ficar no padrao do calendario
            month = robo.parametros.values.get("mes") == null? monthCal.get(Calendar.MONTH) : Integer.valueOf(robo.parametros.values.get("mes")) - 1;
            year = robo.parametros.values.get("ano") == null? monthCal.get(Calendar.YEAR): Integer.valueOf(robo.parametros.values.get("ano"));
            
            //Atualiza o calendario
            monthCal.set(Calendar.DATE, 1);
            monthCal.set(Calendar.YEAR, year);
            monthCal.set(Calendar.MONTH, month);
            
            nomeApp += month + "/" + year;

            robo.executar(start());
        } catch (Exception e) {
            e.printStackTrace();
            FileManager.save(new File(System.getProperty("user.home")) + "\\Desktop\\JavaError.txt", getStackTrace(e));
            System.out.println("Ocorreu um erro na aplicação: " + e);
            System.exit(0);
        }
    }

    private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }

    public static String start() {
        Controller controller = new Controller();

        Map<String, Executavel> execs = new LinkedHashMap<>();
        execs.put("1 - Pegando usos do G:", controller.new getServerUses());
        execs.put("2 - Pegando usos do ZAC", controller.new getZACUses());
        execs.put("3 - Criando tabela de usos", controller.new createTableUses());

        return AppRobo.rodarExecutaveis(nomeApp, execs);
    }

}
