package robot.moresco.countusedrobots.Model;

import fileManager.FileManager;
import Dates.Dates;
import java.util.Calendar;
import java.util.HashMap;
import static robot.moresco.countusedrobots.Controller.uses;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.month;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.year;

public class ServerFiles_Model {

    public static String[] fileUsesLines = FileManager.getText(FileManager.getFile("\\\\HEIMERDINGER\\docs\\Informatica\\Programas\\Moresco\\02 - Arquivos de Programas\\programas_usados.csv")).split("\r\n");

    public static void getFileUsesMap() {
        for (String line : fileUsesLines) {
            String[] cols = line.split(";", -1);

            //Pega data
            Calendar cal = Dates.getCalendarFromFormat(cols[0].split(" ")[0], "yyyy-MM-dd");

            //Se for do mes definido
            if (cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year) {
                //Se nao tiver o mapa da tarefa cria
                uses.putIfAbsent(cols[2], new HashMap<>());

                //Se não tiver aquele usuario na tarefa coloca
                uses.get(cols[2]).putIfAbsent(cols[1], 0);

                //Coloca no usuario e tarefa + 1
                uses.get(cols[2]).put(cols[1], uses.get(cols[2]).get(cols[1]) + 1);
            }
        }
    }
}
