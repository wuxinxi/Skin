package com.wxx.sqliteapp.entity;

import com.wxx.sqlite.annotation.Entity;
import com.wxx.sqlite.annotation.Id;

/**
 * @author ：wuxinxi on 2020/5/20 .
 * @packages ：com.wxx.sqliteapp.entity .
 * TODO:一句话描述
 */
@Entity
public class TestEntity {
    @Id(autoincrement = true)
    public Long id;
    public String value;
    public String key;

    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
