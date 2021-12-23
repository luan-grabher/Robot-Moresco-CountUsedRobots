package robot.moresco.countusedrobots.Model;

import Robo.Model.Parameters;
import fileManager.FileManager;
import robot.moresco.countusedrobots.Controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private static final String sqlGetZACTasks = FileManager.getText(FileManager.getFile(".\\sql\\getZACTasks.sql"));
    private static final String sqlGetAllZACTasks = FileManager.getText(FileManager.getFile(".\\sql\\getAllZacTasks.sql"));

    public static void addZacUses() {
        Map<String, String> swaps = new HashMap<>();
        swaps.put("date_start", monthCal.get(Calendar.YEAR) + "-" + (monthCal.get(Calendar.MONTH) + 1) + "-01");
        swaps.put("date_end", monthCal.get(Calendar.YEAR) + "-" + (monthCal.get(Calendar.MONTH) + 1) + "-" + monthCal.getLeastMaximum(Calendar.DATE));
        
        //swaps.put("date_start", "2021-01-01");
        //swaps.put("date_end", "2021-12-01");

        Database.setStaticObject(new Database("mysql.cfg"));

        List<Map<String, Object>> monthTasks = Database.getDatabase().getMap(sqlGetZACTasks, swaps);

        for (Map<String, Object> monthTask : monthTasks) {
            String tarefa = monthTask.get("nome").toString();
            String user = (new Parameters(monthTask.get("descricao").toString())).values.get("email").split("@")[0];
            
            //Se a tarefa não estiver em ignoredTasks e o usuário não estiver em ignoredUsers
            if (!Controller.ignoredTasks.containsKey(tarefa) && !Controller.ignoredUsers.containsKey(user)) {
                //Se nao tiver o mapa da TAREFA cria
                uses.putIfAbsent(tarefa, new TreeMap<>());

                //Se não tiver aquele USUARIO na tarefa coloca o usuario e a contagem 0
                uses.get(tarefa).putIfAbsent(user, 0);

                //ADICIONA + USOS Coloca no usuario e tarefa + 1
                uses.get(tarefa).put(user, uses.get(tarefa).get(user) + 1);
            }
        }
        
        List<Map<String, Object>> allTasks = Database.getDatabase().getMap(sqlGetAllZACTasks, new HashMap<>());
        
        for(Map<String,Object> task: allTasks){
            String nome = task.get("nome").toString();
            
            //Se a tarefa não estiver em ignoredTasks e o nome não estiver em uses, adiciona a tarefa como 'TAREFA NÃO UTILIZADA'
            if(!Controller.ignoredTasks.containsKey(nome) && !uses.containsKey(nome)){
                uses.putIfAbsent(nome, new TreeMap<>());
                uses.get(nome).put("TAREFA NÃO UTILIZADA", 0);
            }           
        }
    }
}
