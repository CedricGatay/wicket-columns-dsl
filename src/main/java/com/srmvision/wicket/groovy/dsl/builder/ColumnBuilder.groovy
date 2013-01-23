package com.srmvision.wicket.groovy.dsl.builder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
/**
 * Simple builder for Wicket IColumns
 * Implementation is incomplete, this is just a POC
 *
 *
 * @author cgatay
 */
class ColumnBuilder {
    private Columns c

    ColumnBuilder() {
    }

    def columns(List<String> userColumns = [], @DelegatesTo(Columns) Closure c) {
        this.c = new Columns()
        c.delegate = this.columns
        c()
        build(userColumns)
    }

    private def build(userColumns) {
        c.build(userColumns)
    }
}

class Columns {
    List<Column> columns

    Columns() {
        this.columns = new ArrayList<>()
    }

    def column(@DelegatesTo(Column) Closure c) {
        def column = new Column()
        appendColumnAndChain(column, c)
    }

    def propertyColumn(@DelegatesTo(PropertyColumn) Closure c) {
        def column = new PropertyColumn()
        appendColumnAndChain(column, c)
    }

    def sortableColumn(def sameProperty = true, @DelegatesTo(SortableColumn) Closure c) {
        def column = new SortableColumn(sameProperty)
        appendColumnAndChain(column, c)
    }

    def List<IColumn> build(userColumns) {
        if (userColumns.isEmpty()){
            columns.findAll { it.defaultVisible || it.mandatory } collectAll { it.build() }
        }else{
            columns.findAll { userColumns.contains(it.name) || it.mandatory}.collectAll { it.build() }
        }
    }

    private void appendColumnAndChain(Column column, Closure c) {
        this.columns << column
        c.delegate = column
        c()
    }
}

class Column {
    def name
    def content
    def mandatory
    def defaultVisible

    Column() {
        this.mandatory = false
        this.defaultVisible = true
    }

    def name(String n) {
        this.name = n
    }

    def content(Closure c) {
        this.content = c
    }

    def mandatory(Boolean m){
        this.mandatory = m
    }

    def visible(Boolean v){
        this.defaultVisible = v
    }

    def build() {
        new AbstractColumn(Model.of(name)) {
            @Override
            void populateItem(Item cellItem, String componentId, IModel model) {
                this.content(cellItem, componentId, model)
            }
        }
    }
}

class PropertyColumn extends Column {
    def propertyExpression

    def propertyExpr(String p) {
        this.propertyExpression = p
    }

    def build() {
        new org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn(Model.of(name), propertyExpression)
    }
}
class SortableColumn extends PropertyColumn {
    def sortProperty
    def sameProperty

    SortableColumn(boolean sameProperty) {
        this.sameProperty = sameProperty
    }

    def sortProperty(String n) {
        this.sortProperty = n
        if (sameProperty){
            this.propertyExpr(n)
        }
    }

    def build() {
        new org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn(Model.of(name), sortProperty, propertyExpression)
    }
}
