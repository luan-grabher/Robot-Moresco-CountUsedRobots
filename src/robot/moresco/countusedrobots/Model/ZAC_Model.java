package robot.moresco.countusedrobots.Model;

import Robo.Model.Parameters;
import fileManager.FileManager;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static robot.moresco.countusedrobots.Controller.uses;
import static robot.moresco.countusedrobots.RobotMorescoCountUsedRobots.monthCal;
import sql.Database;

public class ZAC_Model {

    /**
     * Select de todas as tarefas do ZAC do mês
     *
     * Percorre as tarefas -- Se o email não for ti01 ---- Adiciona ao mapa de
     * execs de cada email sem o @
     *
     */
    private static final String sqlGetZACTasks = FileManager.getText(FileManager.getFile("\\sql\\getZACTasks.sql"));

    public static void addZacUses() {
        Map<String, String> swaps = new HashMap<>();
        swaps.put("date_start", monthCal.get(Calendar.YEAR) + "-" + (monthCal.get(Calendar.MONTH) + 1) + "-01");
        swaps.put("date_end", monthCal.get(Calendar.YEAR) + "-" + (monthCal.get(Calendar.MONTH) + 1) + "-" + monthCal.getLeastMaximum(Calendar.DATE));

        Database.setStaticObject(new Database("mysql.cfg"));

        List<Map<String, Object>> monthTasks = Database.getDatabase().getMap(sqlGetZACTasks, swaps);

        for (Map<String, Object> monthTask : monthTasks) {
            String tarefa = monthTask.get("nome").toString();
            String user = (new Parameters(monthTask.get("descricao").toString())).values.get("email").split("@")[0];
            
            //Se nao tiver o mapa da TAREFA cria
            uses.putIfAbsent(tarefa, new HashMap<>());

            //Se não tiver aquele USUARIO na tarefa coloca
            uses.get(tarefa).putIfAbsent(user, 0);

            //ADICIONA + USOS Coloca no usuario e tarefa + 1
            uses.get(tarefa).put(user, uses.get(tarefa).get(user) + 1);
        }
    }
}
