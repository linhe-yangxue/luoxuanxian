package com.ssmCore.mongo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;

@Repository
public class BaseDaoImpl implements I_BaseDao {

    @Resource
    protected MongoTemplate mongoTemplate;

    public static BaseDaoImpl getInstance() {
        return SpringContextUtil.getBean(BaseDaoImpl.class);
    }

    /**
     * 根据主键id返回对象
     *
     * @param id 唯一标识
     * @return T 对象
     */
    public <T> T findById(Class<T> entityClass, String id) {
        return this.mongoTemplate.findById(id, entityClass);
    }

    /**
     * 根据条件查询一条记录
     *
     * @param query
     * @param entityClass
     * @return
     */
    public <T> T find(Query query, Class<T> entityClass) {
        System.out.println(query);
        System.out.println("tag::" + entityClass + "   //" + this.isExits(entityClass));
        return this.mongoTemplate.findOne(query, entityClass);
    }

    /**
     * 根据条件查询一条记录
     *
     * @return
     */
    public CommandResult find(String jsonCommand) {
        return this.mongoTemplate.executeCommand(jsonCommand);

    }

    /**
     * 多值聚合查询
     *
     * @param className
     * @param jsonCommand
     * @return
     */
    public <T> T find(String className, String jsonCommand, Class<T> entityClass) {
        DBCursor cursor = this.mongoTemplate.getCollection(className).find((DBObject) JSON.parse(jsonCommand));
        return JsonTransfer._In(JsonTransfer.getJson(cursor.toArray()), entityClass);
    }

    /**
     * 根据条件查询一条记录
     *
     * @param <T>
     * @param <T>
     * @param <T>
     * @return
     */
    public <T> T find(String classname, String key, String query, Class<T> cls) {
        String jsonCommand = "{distinct:'" + classname + "',key:'" + key + "',query:{" + query + "}}";
        CommandResult result = this.mongoTemplate.executeCommand(jsonCommand);
        String res = result.get("values").toString();
        T t = JsonTransfer._In(res, cls);
        return t;
    }

    /**
     * 根据主键id返回对象
     *
     * @param T     要存入的对象
     * @param filed 要查询的实体字段名
     * @param val   要查询的实体字段值
     * @return
     */
    public <T> void findFiled(T t, String filed, T val) {
        Query query = new Query(Criteria.where(filed).is(val));
        if (this.mongoTemplate.find(query, t.getClass()) != null) {
            this.mongoTemplate.save(t);
        } else {
            this.mongoTemplate.insert(t);
        }
    }

    /**
     * 根据类获取全部的对象列表
     *
     * @param entityClass 返回类型
     * @return List<T> 返回对象列表
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        return this.mongoTemplate.findAll(entityClass);
    }

    public <T> List<T> findAll(Query query, Class<T> entityClass) {
        return this.mongoTemplate.find(query, entityClass);
    }

    /**
     * 删除一个对象
     *
     * @param obj 要删除的Mongo对象
     */
    public void remove(Object obj) {
        this.mongoTemplate.remove(obj);
    }

    public <T> void remove(Query query, Class<T> entityClass) {
        this.mongoTemplate.remove(query, entityClass);
    }

    /**
     * 添加对象
     *
     * @param obj 要添加的Mongo对象
     */
    public void add(Object obj) {
        this.mongoTemplate.insert(obj);
    }

    /**
     * 批量插入
     *
     * @param objs
     */
    public void add(Collection<? extends Object> objs) {
        this.mongoTemplate.insertAll(objs);
    }

    /**
     * 修改对象
     *
     * @param obj 要修改的Mongo对象
     */
    public void saveOrUpdate(Object obj) {
        this.mongoTemplate.save(obj);
    }

    /**
     * 批量更新
     *
     * @param query
     * @param update
     * @param entityClass
     */
    public void saveOrUpdate(Query query, Update update, Class<?> entityClass) {
        this.mongoTemplate.updateMulti(query, update, entityClass);
    }

    /**
     * 插入或更新
     *
     * @param query
     * @param update
     * @param entityClass
     */
    public void insertOrUpdate(Query query, Update update, Class<?> entityClass) {
        this.mongoTemplate.upsert(query, update, entityClass);
    }

    public void updatFirst(Query query, Update update, Class<?> entityClass) {
        this.mongoTemplate.updateFirst(query, update, entityClass);
    }

    /**
     * @param entityClass 查询对象
     * @param query       查询条件
     * @return
     */
    public <T> Long count(Class<T> entityClass, Query query) {
        return this.mongoTemplate.count(query, entityClass);
    }

    /**
     * 批量插入
     *
     * @param entityClass 对象类
     * @param collection  要插入的对象集合
     */
    public <T> void addCollection(Class<T> entityClass, Collection<T> collection) {
        this.mongoTemplate.insert(collection, entityClass);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    /**
     * 删除集合对象
     *
     * @param name
     */
    public <T> void dorp(String name) {
        DBCollection dbCol = mongoTemplate.getCollection(name);
        dbCol.drop();
    }

    /**
     * 判断集合是否存在
     *
     * @param entityClass
     * @return
     */
    public <T> boolean isExits(Class<?> clz) {
        if (count(clz, new Query()) > 0)
            return true;
        return false;
    }

    /**
     * 查询并分页
     *
     * @param entityClass
     *            对象类型
     * @param query
     *            查询条件
     * @param page
     *            分页
     * @return
     */
    /**
     * 通过条件查询,查询分页结果
     *
     * @param <T>
     * @param <T>
     * @param pageNo
     * @param pageSize
     * @param query
     * @return
     */
    public <T> PageList PaginationgetPage(int pageNo, int pageSize, Query query, Class<T> clzz) {
        long totalCount = this.mongoTemplate.count(query, clzz);
        PageList page = new PageList(pageNo, pageSize, totalCount);
        query.skip(page.getFirstResult());// skip相当于从那条记录开始
        query.limit(pageSize);// 从skip开始,取多少条记录
        List<T> datas = this.mongoTemplate.find(query, clzz);
        datas.removeAll(Collections.singleton(null));
        page.setDatas(datas);
        return page;
    }

    public void setMongoTemplet(MongoTemplate mongo) {
        this.mongoTemplate = mongo;
    }
}
