package com.test.hermes.process;


import com.lee.hermes.annotion.ClassId;

/**
 * 映射子类
 * @author jv.lee
 * classId一定不能错
 */
@ClassId(value = "com.test.hermes.process.UserManager")
public interface IUserManager {
    Person getPerson();
    void setPerson(Person person);
}
