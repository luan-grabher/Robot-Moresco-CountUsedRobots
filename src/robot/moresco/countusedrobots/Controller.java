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

    public static Section ignoredUsers =(Section)  ini.get("ignored users");
    public static Section ignoredTasks =(Section) ini.get("ignored tasks");;
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

    public class createUsesHtmlResponse extends Executavel {

        @Override
        public void run() {
            //TODO: Criar tabela html de tarefas sem duplicidade com total de usos
            //TODO: Criar tabela html de usuarios com total de usos
            //TODO: Criar tabela html de tarefas com usuarios e usos por tarefa
            
            //Html table with total uses per users per task --> Tarefa --> Usuario --> Contagem
            StringBuilder taskUserUsesTable = new  StringBuilder();
            taskUserUsesTable.append("<table border=\"1\">");
            taskUserUsesTable.append("<tr>");
            taskUserUsesTable.append("<th>Tarefa</th>");
            taskUserUsesTable.append("<th>Usuario</th>");
            taskUserUsesTable.append("<th>Contagem</th>");
            taskUserUsesTable.append("</tr>");
            
            for (Map.Entry<String, Map<String, Integer>> task : uses.entrySet()) {
                for (Map.Entry<String, Integer> user : task.getValue().entrySet()) {
                    //Table with total uses per users per task
                    taskUserUsesTable.append("<tr>");
                    taskUserUsesTable.append("<td>").append(task.getKey()).append("</td>");
                    taskUserUsesTable.append("<td>").append(user.getKey()).append("</td>");
                    taskUserUsesTable.append("<td>").append(user.getValue()).append("</td>");
                    taskUserUsesTable.append("</tr>");
                }
            }
            //close table
            taskUserUsesTable.append("</table>");
            
            //tasksUses -> Html table with total uses per tasks with unique tasks
            StringBuilder tasksUsesTable = new  StringBuilder();
            tasksUsesTable.append("<table border=\"1\">");
            tasksUsesTable.append("<tr>");
            tasksUsesTable.append("<th>Tarefa</th>");
            tasksUsesTable.append("<th>Contagem</th>");
            tasksUsesTable.append("</tr>");
            
            //Create new map with unique tasks
            Map<String, Integer> tasksUses = new TreeMap<>();
            for (Map.Entry<String, Map<String, Integer>> task : uses.entrySet()) {
                for (Map.Entry<String, Integer> user : task.getValue().entrySet()) {
                    if(tasksUses.containsKey(task.getKey())){
                        tasksUses.put(task.getKey(), tasksUses.get(task.getKey()) + user.getValue());
                    }else{
                        tasksUses.put(task.getKey(), user.getValue());
                    }
                }
            }
            //Create trs with unique tasks and total uses
            for (Map.Entry<String, Integer> task : tasksUses.entrySet()) {
                //Table with total uses per tasks with unique tasks
                tasksUsesTable.append("<tr>");
                tasksUsesTable.append("<td>").append(task.getKey()).append("</td>");
                tasksUsesTable.append("<td>").append(task.getValue()).append("</td>");
                tasksUsesTable.append("</tr>");
            }
            //close table
            tasksUsesTable.append("</table>");

            //usersUses -> Html table with total uses per users with unique users
            StringBuilder usersUsesTable = new  StringBuilder();
            usersUsesTable.append("<table border=\"1\">");
            usersUsesTable.append("<tr>");
            usersUsesTable.append("<th>Usuario</th>");
            usersUsesTable.append("<th>Contagem</th>");
            usersUsesTable.append("</tr>");

            //Create new map with unique users
            Map<String, Integer> usersUses = new TreeMap<>();
            for (Map.Entry<String, Map<String, Integer>> task : uses.entrySet()) {
                for (Map.Entry<String, Integer> user : task.getValue().entrySet()) {
                    if(usersUses.containsKey(user.getKey())){
                        usersUses.put(user.getKey(), usersUses.get(user.getKey()) + user.getValue());
                    }else{
                        usersUses.put(user.getKey(), user.getValue());
                    }
                }
            }

            //Create trs with unique users and total uses
            for (Map.Entry<String, Integer> user : usersUses.entrySet()) {
                //Table with total uses per users with unique users
                usersUsesTable.append("<tr>");
                usersUsesTable.append("<td>").append(user.getKey()).append("</td>");
                usersUsesTable.append("<td>").append(user.getValue()).append("</td>");
                usersUsesTable.append("</tr>");
            }
            //close table
            usersUsesTable.append("</table>");


            //Using taskUses, in usedTasks count tasks useds when count is greater than 0, and not used when count is 0
            for (Map.Entry<String, Map<String, Integer>> task : uses.entrySet()) {
                for (Map.Entry<String, Integer> user : task.getValue().entrySet()) {
                    if(user.getValue() > 0){
                        usedTasks++;
                    }else{
                        notUsedTasks++;
                    }
                }
            }


            //Total de tarefas usadas e tarefas não usadas
            StringBuilder totalTasks = new  StringBuilder();
            totalTasks.append("<table border=\"1\">");
            totalTasks.append("<tr>");
            totalTasks.append("<th>Total de tarefas usadas</th>");
            totalTasks.append("<th>Total de tarefas não usadas</th>");
            totalTasks.append("</tr>");
            totalTasks.append("<tr>");
            totalTasks.append("<td>").append(usedTasks).append("</td>");
            totalTasks.append("<td>").append(notUsedTasks).append("</td>");
            totalTasks.append("</tr>");
            totalTasks.append("</table>");

            //unify all tables in StringBuilder and show in throw new Warning
            StringBuilder allTables = new StringBuilder("<br><br>");

            //Tarefas usadas e não usadas
            allTables.append(totalTasks);
            allTables.append("<h1>Tabela de usos por tarefa</h1>");
            allTables.append(tasksUsesTable);
            allTables.append("<h1>Tabela de usos por usuario</h1>");
            allTables.append(usersUsesTable);
            allTables.append("<h1>Tabela de usos por tarefa com usuarios e usos por tarefa</h1>");
            allTables.append(taskUserUsesTable);            
            

            //Show all tables in throw new Warning
            throw new Warning(allTables.toString());
        }

    }

    public class getServerUses extends Executavel {

        @Override
        public void run() {
            ServerFiles_Model.getFileUsesMap();
        }
    }
}
