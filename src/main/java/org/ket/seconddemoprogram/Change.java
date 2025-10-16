package org.ket.seconddemoprogram;

public class Change {
    private int indexList;
    private String name;
    private final OperationType operationType;

    enum OperationType {
        CREATE("Создание"),
        DELETE("Удаление");

        private final String info;

        OperationType(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    public Change(int indexList, String name, OperationType operationType) {
        this.indexList = indexList;
        this.name = name;
        this.operationType = operationType;
    }

    public int getIndexList() {
        return indexList;
    }

    public void setIndexList(int indexList) {
        this.indexList = indexList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperationType getOperationType() {
        return operationType;
    }
}