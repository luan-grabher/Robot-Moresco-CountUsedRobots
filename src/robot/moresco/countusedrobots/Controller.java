package robot.moresco.countusedrobots;

import Entity.Executavel;
import Entity.Warning;
import java.util.HashMap;
import java.util.Map;
import robot.moresco.countusedrobots.Model.ServerFiles_Model;

public class Controller {

    // Tarefa --> Usuario --> Contagem
    public static Map<String, Map<String, Integer>> uses = new HashMap<>();

    public class getZACUses extends Executavel {

        @Override
        public void run() {
            /**
             * Select de todas as tarefas do ZAC do mês
             *
             * Percorre as tarefas -- Se o email não for ti01 ---- Adiciona ao
             * mapa de execs de cada email sem o @
             *
             */
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
                    table.append("<tr>");
                    table.append("<td>").append(task).append("</td>");
                    table.append("<td>").append(user).append("</td>");
                    table.append("<td>").append(count).append("</td>");
                    table.append("</tr>");
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
