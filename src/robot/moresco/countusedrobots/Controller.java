package robot.moresco.countusedrobots;

import Entity.Executavel;
import Entity.Warning;
import java.util.Map;
import java.util.TreeMap;
import org.ini4j.Profile.Section;
import robot.moresco.countusedrobots.Model.ServerFiles_Model;
import robot.moresco.countusedrobots.Model.ZAC_Model;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.ini;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.month;

public class Controller {    

    public static Section ignoreUsers =(Section)  ini.get("ignore users");
    public static Section ignoreTasks =(Section) ini.get("ignore tasks");;
    public Integer usedTasks = 0;
    public Integer notUsedTasks = 0;

    // Tarefa --> Usuario --> Contagem
    public static Map<String, Map<String, Integer>> uses = new TreeMap<>();

    public class getZACUses extends Executavel {

        @Override
        public void run() {
            ZAC_Model.addZacUses();
        }

    }

    public class createTableUses extends Executavel {

        @Override
        public void run() {
            StringBuilder table = new StringBuilder("<table style='border: 1px solid'>");
            table.append("<th>");
            table.append("<td>Tarefa</td><td>Usuário</td><td>Utilizações</td>");
            table.append("</th>");

            uses.forEach((task, users) -> {
                Boolean[] ignored = new Boolean[]{false};
                
                users.forEach((user, count) -> {
                    if (!ignoreUsers.containsKey(user) && !ignoreTasks.containsKey(task)) {
                        table.append("<tr>");
                        table.append("<td>").append(task).append("</td>");
                        table.append("<td>").append(user).append("</td>");
                        table.append("<td>").append(count).append("</td>");
                        table.append("</tr>");
                        
                        if(count > 0){
                            usedTasks++;
                        }else{
                            notUsedTasks++;
                        }
                    }
                });                
            });

            table.append("</table>");
            
            System.out.println(uses);
            
            StringBuilder taskUses = new StringBuilder();
            taskUses.append("Total de robôs no mês").append((month + 1)).append(": ").append((usedTasks+notUsedTasks));
            taskUses.append("<br>Robôs utilizados: ").append(usedTasks);
            taskUses.append("<br>Robôs NÃO utilizados: ").append(notUsedTasks);
            taskUses.append("<br><br>").append(table.toString());
            

            throw new Warning(taskUses.toString());
        }

    }

    public class getServerUses extends Executavel {

        @Override
        public void run() {
            ServerFiles_Model.getFileUsesMap();
        }
    }
}
