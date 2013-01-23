import groovy.transform.ToString

/**
 * User: cgatay
 * Date: 21/01/13
 * Time: 19:27
 */
@ToString
class UserColumns {
    private CustomColumns columns

    UserColumns(){
        columns = new CustomColumns()
    }

    def settings(Closure c){
        c.delegate = this.columns
        c()
        this
    }

    def isEmpty(){
        columns.userColumns.isEmpty()
    }

    def List<String> userColumns(){
        columns.userColumns
    }
}
@ToString
class CustomColumns{
    def List<String> userColumns
    def defaultSort

    CustomColumns() {
        this.userColumns = new ArrayList<String>()
    }

    def column(List<String> col){
        userColumns = col
    }

    def sort(col, asc){
        defaultSort = [column : col, ascending : asc]
    }
}