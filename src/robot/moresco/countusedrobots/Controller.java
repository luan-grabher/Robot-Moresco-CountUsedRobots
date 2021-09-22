package robot.moresco.countusedrobots;

import Entity.Executavel;
import Entity.Warning;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import robot.moresco.countusedrobots.Model.ServerFiles_Model;
import robot.moresco.countusedrobots.Model.ZAC_Model;

public class Controller {

    public static List<String> ignoreUsers = new ArrayList<String>(Arrays.asList("ti01"));
    public static List<String> ignoreTasks = new ArrayList<String>(Arrays.asList("InsertOutlookSignature"));

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
                users.forEach((user, count) -> {
                    if (!ignoreUsers.contains(user) && !ignoreTasks.contains(task)) {
                        table.append("<tr>");
                        table.append("<td>").append(task).append("</td>");
                        table.append("<td>").append(user).append("</td>");
                        table.append("<td>").append(count).append("</td>");
                        table.append("</tr>");
                    }
                });
            });

            table.append("</table>");

            throw new Warning(table.toString());
        }

    }

    public class getServerUses extends Executavel {

        @Override
        public void run() {
            ServerFiles_Model.getFileUsesMap();
        }
    }
}
