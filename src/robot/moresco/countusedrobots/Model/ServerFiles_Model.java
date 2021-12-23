package robot.moresco.countusedrobots.Model;

import fileManager.FileManager;
import Dates.Dates;
import java.util.Calendar;
import java.util.TreeMap;
import robot.moresco.countusedrobots.Controller;
import static robot.moresco.countusedrobots.Controller.uses;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.ini;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.month;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.year;

public class ServerFiles_Model {

    public static String[] fileUsesLines = FileManager.getText(FileManager.getFile("\\\\HEIMERDINGER\\docs\\Informatica\\Programas\\Moresco\\02 - Arquivos de Programas\\programas_usados.csv")).split("\r\n");
    public static String[] tasksOnFolder = FileManager.getFile("\\\\HEIMERDINGER\\docs\\Informatica\\Programas\\Moresco\\01 - Programas").list();

    public static void getFileUsesMap() {
        //Tarefa --> Usuario --> Contagem
        for (String line : fileUsesLines) {
            String[] cols = line.split(";", -1);

            //Pega data
            Calendar cal = Dates.getCalendarFromFormat(cols[0].split(" ")[0], "yyyy-MM-dd");

            //Se for do mes definido
            if (cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year) {
                String tarefa = cols[2];

                if (ini.get("replace", tarefa) != null) {
                    tarefa = ini.get("replace", tarefa);
                }

                //Se a tarefa não estiver em ignoredTasks e o usuário não estiver em ignoredUsers
                if (!Controller.ignoredTasks.containsKey(tarefa) && !Controller.ignoredUsers.containsKey(cols[1])) {
                    //Se nao tiver o mapa da TAREFA cria
                    uses.putIfAbsent(tarefa, new TreeMap<>());

                    //Se não tiver aquele USUARIO na tarefa coloca
                    uses.get(tarefa).putIfAbsent(cols[1], 0);

                    //ADICIONA +USOS Coloca no usuario e tarefa + 1
                    uses.get(tarefa).put(cols[1], uses.get(tarefa).get(cols[1]) + 1);
                }
            }
        }

        //Pega as tarefas que não foram utilizadas
        for (String tarefa : tasksOnFolder) {
            //remove extensao
            tarefa = tarefa.split("\\.")[0];
            
            //se tiver replace no nome coloca
            if (ini.get("replace", tarefa) != null) {
                tarefa = ini.get("replace", tarefa);
            }
            
            //Se a tarefa não estiver em 'ignore tasks' e não estiver no mapa de usos
            if(!Controller.ignoredTasks.containsKey(tarefa) && !uses.containsKey(tarefa)){
                uses.putIfAbsent(tarefa, new TreeMap<>());
                uses.get(tarefa).put("TAREFA NÃO UTILIZADA", 0);
            }
        }
    }
}
