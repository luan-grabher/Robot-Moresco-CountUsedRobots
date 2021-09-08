package robot.moresco.countusedrobots;

import Entity.Executavel;
import Robo.AppRobo;
import fileManager.FileManager;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RobotMorescoCountUsedRobots {

    public static String testParameters = "";
    public static String nomeApp = "";
    public static Integer month = 1;
    public static Integer year = 2021;

    public static void main(String[] args) {

        try {

            nomeApp = "Utilização dos robôs mensal ";

            AppRobo robo = new AppRobo(nomeApp);
            robo.definirParametros();

            if (args.length > 0 && args[0].equals("test")) {
                robo.definirParametros(testParameters);
            }

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            month = robo.parametros.values.get("mes") == null? cal.get(Calendar.MONTH): Integer.valueOf(robo.parametros.values.get("mes"));
            year = robo.parametros.values.get("ano") == null? cal.get(Calendar.YEAR): Integer.valueOf(robo.parametros.values.get("ano"));;
            
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

        Map<String, Executavel> execs = new HashMap<>();
        execs.put("Pegando usos do G:", controller.new getServerUses());
        execs.put("Pegando usos do ZAC", controller.new getZACUses());
        execs.put("Criando tabela de usos", controller.new createTableUses());

        return AppRobo.rodarExecutaveis(nomeApp, execs);
    }

}
