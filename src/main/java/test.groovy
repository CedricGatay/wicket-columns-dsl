import com.srmvision.wicket.groovy.dsl.builder.ColumnBuilder

def builder = new ColumnBuilder()

def userColumns = new UserColumns().settings {
    column (["wondelle", "itemName","currentDate", "actions"])
    sort  "currentDate", false
}

def columnDefinition = {
    sortableColumn(false) {
        name "SortableColumn"
        sortProperty "sort.property"
        propertyExpr "sort.property2"
    }
    sortableColumn {
        name "Column"
        sortProperty "sort.property"
        mandatory true
    }
    propertyColumn {
        name "currentDate"
        propertyExpr "sort.currentDate"
    }
    propertyColumn {
        name "itemName"
        propertyExpr "sort.property"
    }
    column {
        name "NormalColumn"
        content { cellItem, componentId, model ->
            //create component here
        }
    }
    column {
        name "actions"
    }
    column {
        name "actions2"
        mandatory true
    }
}

def buildUserSettings  = builder.columns(userColumns.userColumns(), columnDefinition)
println buildUserSettings

println "==============="

def build  = builder.columns(columnDefinition)
println build