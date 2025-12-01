package reports

/**
 * Created by swapna on 12/18/17.
 */

String jpql = "SELECT t.name, t.status, t.percentComplete, t.plannedStartDate, t.plannedFinishDate, t.actualFinishDate FROM ISTask t where t.status <> 'FINISHED' OR t.actualFinishDate > t.plannedFinishDate";


def tasks = _entityManager.createQuery(jpql).getResultList();

def arr =[];
for(task in tasks) {
    def unfinishedTask = [:]
    unfinishedTask['name'] = task[0];
    unfinishedTask['status'] = task[1];
    unfinishedTask['percent'] = task[2];

    def startDate = new Date(task[3].getTime());
    def start = startDate.format('dd/MM/yyyy');

    def finishDate = new Date(task[4].getTime());
    def finish = finishDate.format('dd/MM/yyyy');

    unfinishedTask['start'] = start;
    unfinishedTask['finish'] = finish;

    if(task[5] == null){
        unfinishedTask['actualFinish'] = null;
    }
    else {
        def actualFinishDate = new Date(task[5].getTime());
        def actualFinish = actualFinishDate.format('dd/MM/yyyy');
        unfinishedTask['actualFinish'] = actualFinish;
    }

    arr.add(unfinishedTask)
}
return arr;



