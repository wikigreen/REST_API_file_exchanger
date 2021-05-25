package com.wikigreen.model;

public class Test {
    private Long id;
    private String name;
    private int age;

    public Test() {

    }

    public Test(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Test(Long id, int age) {
        this.id = id;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
