package com.ssmLogin.defdata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class CensuseCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CensuseCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andGidIsNull() {
            addCriterion("gid is null");
            return (Criteria) this;
        }

        public Criteria andGidIsNotNull() {
            addCriterion("gid is not null");
            return (Criteria) this;
        }

        public Criteria andGidEqualTo(String value) {
            addCriterion("gid =", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidNotEqualTo(String value) {
            addCriterion("gid <>", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidGreaterThan(String value) {
            addCriterion("gid >", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidGreaterThanOrEqualTo(String value) {
            addCriterion("gid >=", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidLessThan(String value) {
            addCriterion("gid <", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidLessThanOrEqualTo(String value) {
            addCriterion("gid <=", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidLike(String value) {
            addCriterion("gid like", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidNotLike(String value) {
            addCriterion("gid not like", value, "gid");
            return (Criteria) this;
        }

        public Criteria andGidIn(List<String> values) {
            addCriterion("gid in", values, "gid");
            return (Criteria) this;
        }

        public Criteria andGidNotIn(List<String> values) {
            addCriterion("gid not in", values, "gid");
            return (Criteria) this;
        }

        public Criteria andGidBetween(String value1, String value2) {
            addCriterion("gid between", value1, value2, "gid");
            return (Criteria) this;
        }

        public Criteria andGidNotBetween(String value1, String value2) {
            addCriterion("gid not between", value1, value2, "gid");
            return (Criteria) this;
        }

        public Criteria andLgdateIsNull() {
            addCriterion("lgdate is null");
            return (Criteria) this;
        }

        public Criteria andLgdateIsNotNull() {
            addCriterion("lgdate is not null");
            return (Criteria) this;
        }

        public Criteria andLgdateEqualTo(Date value) {
            addCriterionForJDBCDate("lgdate =", value, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateNotEqualTo(Date value) {
            addCriterionForJDBCDate("lgdate <>", value, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateGreaterThan(Date value) {
            addCriterionForJDBCDate("lgdate >", value, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("lgdate >=", value, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateLessThan(Date value) {
            addCriterionForJDBCDate("lgdate <", value, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("lgdate <=", value, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateIn(List<Date> values) {
            addCriterionForJDBCDate("lgdate in", values, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateNotIn(List<Date> values) {
            addCriterionForJDBCDate("lgdate not in", values, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("lgdate between", value1, value2, "lgdate");
            return (Criteria) this;
        }

        public Criteria andLgdateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("lgdate not between", value1, value2, "lgdate");
            return (Criteria) this;
        }

        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(String value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(String value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(String value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(String value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(String value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(String value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLike(String value) {
            addCriterion("pid like", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotLike(String value) {
            addCriterion("pid not like", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<String> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<String> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(String value1, String value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(String value1, String value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andZidIsNull() {
            addCriterion("zid is null");
            return (Criteria) this;
        }

        public Criteria andZidIsNotNull() {
            addCriterion("zid is not null");
            return (Criteria) this;
        }

        public Criteria andZidEqualTo(Integer value) {
            addCriterion("zid =", value, "zid");
            return (Criteria) this;
        }

        public Criteria andZidNotEqualTo(Integer value) {
            addCriterion("zid <>", value, "zid");
            return (Criteria) this;
        }

        public Criteria andZidGreaterThan(Integer value) {
            addCriterion("zid >", value, "zid");
            return (Criteria) this;
        }

        public Criteria andZidGreaterThanOrEqualTo(Integer value) {
            addCriterion("zid >=", value, "zid");
            return (Criteria) this;
        }

        public Criteria andZidLessThan(Integer value) {
            addCriterion("zid <", value, "zid");
            return (Criteria) this;
        }

        public Criteria andZidLessThanOrEqualTo(Integer value) {
            addCriterion("zid <=", value, "zid");
            return (Criteria) this;
        }

        public Criteria andZidIn(List<Integer> values) {
            addCriterion("zid in", values, "zid");
            return (Criteria) this;
        }

        public Criteria andZidNotIn(List<Integer> values) {
            addCriterion("zid not in", values, "zid");
            return (Criteria) this;
        }

        public Criteria andZidBetween(Integer value1, Integer value2) {
            addCriterion("zid between", value1, value2, "zid");
            return (Criteria) this;
        }

        public Criteria andZidNotBetween(Integer value1, Integer value2) {
            addCriterion("zid not between", value1, value2, "zid");
            return (Criteria) this;
        }

        public Criteria andNewUserIsNull() {
            addCriterion("newUser is null");
            return (Criteria) this;
        }

        public Criteria andNewUserIsNotNull() {
            addCriterion("newUser is not null");
            return (Criteria) this;
        }

        public Criteria andNewUserEqualTo(Integer value) {
            addCriterion("newUser =", value, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserNotEqualTo(Integer value) {
            addCriterion("newUser <>", value, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserGreaterThan(Integer value) {
            addCriterion("newUser >", value, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("newUser >=", value, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserLessThan(Integer value) {
            addCriterion("newUser <", value, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserLessThanOrEqualTo(Integer value) {
            addCriterion("newUser <=", value, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserIn(List<Integer> values) {
            addCriterion("newUser in", values, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserNotIn(List<Integer> values) {
            addCriterion("newUser not in", values, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserBetween(Integer value1, Integer value2) {
            addCriterion("newUser between", value1, value2, "newUser");
            return (Criteria) this;
        }

        public Criteria andNewUserNotBetween(Integer value1, Integer value2) {
            addCriterion("newUser not between", value1, value2, "newUser");
            return (Criteria) this;
        }

        public Criteria andOldUserIsNull() {
            addCriterion("oldUser is null");
            return (Criteria) this;
        }

        public Criteria andOldUserIsNotNull() {
            addCriterion("oldUser is not null");
            return (Criteria) this;
        }

        public Criteria andOldUserEqualTo(Integer value) {
            addCriterion("oldUser =", value, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserNotEqualTo(Integer value) {
            addCriterion("oldUser <>", value, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserGreaterThan(Integer value) {
            addCriterion("oldUser >", value, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("oldUser >=", value, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserLessThan(Integer value) {
            addCriterion("oldUser <", value, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserLessThanOrEqualTo(Integer value) {
            addCriterion("oldUser <=", value, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserIn(List<Integer> values) {
            addCriterion("oldUser in", values, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserNotIn(List<Integer> values) {
            addCriterion("oldUser not in", values, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserBetween(Integer value1, Integer value2) {
            addCriterion("oldUser between", value1, value2, "oldUser");
            return (Criteria) this;
        }

        public Criteria andOldUserNotBetween(Integer value1, Integer value2) {
            addCriterion("oldUser not between", value1, value2, "oldUser");
            return (Criteria) this;
        }

        public Criteria andActUserIsNull() {
            addCriterion("actUser is null");
            return (Criteria) this;
        }

        public Criteria andActUserIsNotNull() {
            addCriterion("actUser is not null");
            return (Criteria) this;
        }

        public Criteria andActUserEqualTo(Integer value) {
            addCriterion("actUser =", value, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserNotEqualTo(Integer value) {
            addCriterion("actUser <>", value, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserGreaterThan(Integer value) {
            addCriterion("actUser >", value, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("actUser >=", value, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserLessThan(Integer value) {
            addCriterion("actUser <", value, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserLessThanOrEqualTo(Integer value) {
            addCriterion("actUser <=", value, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserIn(List<Integer> values) {
            addCriterion("actUser in", values, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserNotIn(List<Integer> values) {
            addCriterion("actUser not in", values, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserBetween(Integer value1, Integer value2) {
            addCriterion("actUser between", value1, value2, "actUser");
            return (Criteria) this;
        }

        public Criteria andActUserNotBetween(Integer value1, Integer value2) {
            addCriterion("actUser not between", value1, value2, "actUser");
            return (Criteria) this;
        }

        public Criteria andTime_leftIsNull() {
            addCriterion("time_left is null");
            return (Criteria) this;
        }

        public Criteria andTime_leftIsNotNull() {
            addCriterion("time_left is not null");
            return (Criteria) this;
        }

        public Criteria andTime_leftEqualTo(Float value) {
            addCriterion("time_left =", value, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftNotEqualTo(Float value) {
            addCriterion("time_left <>", value, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftGreaterThan(Float value) {
            addCriterion("time_left >", value, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftGreaterThanOrEqualTo(Float value) {
            addCriterion("time_left >=", value, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftLessThan(Float value) {
            addCriterion("time_left <", value, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftLessThanOrEqualTo(Float value) {
            addCriterion("time_left <=", value, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftIn(List<Float> values) {
            addCriterion("time_left in", values, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftNotIn(List<Float> values) {
            addCriterion("time_left not in", values, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftBetween(Float value1, Float value2) {
            addCriterion("time_left between", value1, value2, "time_left");
            return (Criteria) this;
        }

        public Criteria andTime_leftNotBetween(Float value1, Float value2) {
            addCriterion("time_left not between", value1, value2, "time_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftIsNull() {
            addCriterion("day3_left is null");
            return (Criteria) this;
        }

        public Criteria andDay3_leftIsNotNull() {
            addCriterion("day3_left is not null");
            return (Criteria) this;
        }

        public Criteria andDay3_leftEqualTo(Float value) {
            addCriterion("day3_left =", value, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftNotEqualTo(Float value) {
            addCriterion("day3_left <>", value, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftGreaterThan(Float value) {
            addCriterion("day3_left >", value, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftGreaterThanOrEqualTo(Float value) {
            addCriterion("day3_left >=", value, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftLessThan(Float value) {
            addCriterion("day3_left <", value, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftLessThanOrEqualTo(Float value) {
            addCriterion("day3_left <=", value, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftIn(List<Float> values) {
            addCriterion("day3_left in", values, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftNotIn(List<Float> values) {
            addCriterion("day3_left not in", values, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftBetween(Float value1, Float value2) {
            addCriterion("day3_left between", value1, value2, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay3_leftNotBetween(Float value1, Float value2) {
            addCriterion("day3_left not between", value1, value2, "day3_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftIsNull() {
            addCriterion("day7_left is null");
            return (Criteria) this;
        }

        public Criteria andDay7_leftIsNotNull() {
            addCriterion("day7_left is not null");
            return (Criteria) this;
        }

        public Criteria andDay7_leftEqualTo(Float value) {
            addCriterion("day7_left =", value, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftNotEqualTo(Float value) {
            addCriterion("day7_left <>", value, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftGreaterThan(Float value) {
            addCriterion("day7_left >", value, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftGreaterThanOrEqualTo(Float value) {
            addCriterion("day7_left >=", value, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftLessThan(Float value) {
            addCriterion("day7_left <", value, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftLessThanOrEqualTo(Float value) {
            addCriterion("day7_left <=", value, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftIn(List<Float> values) {
            addCriterion("day7_left in", values, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftNotIn(List<Float> values) {
            addCriterion("day7_left not in", values, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftBetween(Float value1, Float value2) {
            addCriterion("day7_left between", value1, value2, "day7_left");
            return (Criteria) this;
        }

        public Criteria andDay7_leftNotBetween(Float value1, Float value2) {
            addCriterion("day7_left not between", value1, value2, "day7_left");
            return (Criteria) this;
        }

        public Criteria andNewpayIsNull() {
            addCriterion("newpay is null");
            return (Criteria) this;
        }

        public Criteria andNewpayIsNotNull() {
            addCriterion("newpay is not null");
            return (Criteria) this;
        }

        public Criteria andNewpayEqualTo(Float value) {
            addCriterion("newpay =", value, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayNotEqualTo(Float value) {
            addCriterion("newpay <>", value, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayGreaterThan(Float value) {
            addCriterion("newpay >", value, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayGreaterThanOrEqualTo(Float value) {
            addCriterion("newpay >=", value, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayLessThan(Float value) {
            addCriterion("newpay <", value, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayLessThanOrEqualTo(Float value) {
            addCriterion("newpay <=", value, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayIn(List<Float> values) {
            addCriterion("newpay in", values, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayNotIn(List<Float> values) {
            addCriterion("newpay not in", values, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayBetween(Float value1, Float value2) {
            addCriterion("newpay between", value1, value2, "newpay");
            return (Criteria) this;
        }

        public Criteria andNewpayNotBetween(Float value1, Float value2) {
            addCriterion("newpay not between", value1, value2, "newpay");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(Float value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(Float value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(Float value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(Float value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(Float value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(Float value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<Float> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<Float> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(Float value1, Float value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(Float value1, Float value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andPayRateIsNull() {
            addCriterion("payRate is null");
            return (Criteria) this;
        }

        public Criteria andPayRateIsNotNull() {
            addCriterion("payRate is not null");
            return (Criteria) this;
        }

        public Criteria andPayRateEqualTo(Float value) {
            addCriterion("payRate =", value, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateNotEqualTo(Float value) {
            addCriterion("payRate <>", value, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateGreaterThan(Float value) {
            addCriterion("payRate >", value, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateGreaterThanOrEqualTo(Float value) {
            addCriterion("payRate >=", value, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateLessThan(Float value) {
            addCriterion("payRate <", value, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateLessThanOrEqualTo(Float value) {
            addCriterion("payRate <=", value, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateIn(List<Float> values) {
            addCriterion("payRate in", values, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateNotIn(List<Float> values) {
            addCriterion("payRate not in", values, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateBetween(Float value1, Float value2) {
            addCriterion("payRate between", value1, value2, "payRate");
            return (Criteria) this;
        }

        public Criteria andPayRateNotBetween(Float value1, Float value2) {
            addCriterion("payRate not between", value1, value2, "payRate");
            return (Criteria) this;
        }

        public Criteria andArpuIsNull() {
            addCriterion("arpu is null");
            return (Criteria) this;
        }

        public Criteria andArpuIsNotNull() {
            addCriterion("arpu is not null");
            return (Criteria) this;
        }

        public Criteria andArpuEqualTo(Float value) {
            addCriterion("arpu =", value, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuNotEqualTo(Float value) {
            addCriterion("arpu <>", value, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuGreaterThan(Float value) {
            addCriterion("arpu >", value, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuGreaterThanOrEqualTo(Float value) {
            addCriterion("arpu >=", value, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuLessThan(Float value) {
            addCriterion("arpu <", value, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuLessThanOrEqualTo(Float value) {
            addCriterion("arpu <=", value, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuIn(List<Float> values) {
            addCriterion("arpu in", values, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuNotIn(List<Float> values) {
            addCriterion("arpu not in", values, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuBetween(Float value1, Float value2) {
            addCriterion("arpu between", value1, value2, "arpu");
            return (Criteria) this;
        }

        public Criteria andArpuNotBetween(Float value1, Float value2) {
            addCriterion("arpu not between", value1, value2, "arpu");
            return (Criteria) this;
        }

        public Criteria andArppuIsNull() {
            addCriterion("arppu is null");
            return (Criteria) this;
        }

        public Criteria andArppuIsNotNull() {
            addCriterion("arppu is not null");
            return (Criteria) this;
        }

        public Criteria andArppuEqualTo(Float value) {
            addCriterion("arppu =", value, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuNotEqualTo(Float value) {
            addCriterion("arppu <>", value, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuGreaterThan(Float value) {
            addCriterion("arppu >", value, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuGreaterThanOrEqualTo(Float value) {
            addCriterion("arppu >=", value, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuLessThan(Float value) {
            addCriterion("arppu <", value, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuLessThanOrEqualTo(Float value) {
            addCriterion("arppu <=", value, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuIn(List<Float> values) {
            addCriterion("arppu in", values, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuNotIn(List<Float> values) {
            addCriterion("arppu not in", values, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuBetween(Float value1, Float value2) {
            addCriterion("arppu between", value1, value2, "arppu");
            return (Criteria) this;
        }

        public Criteria andArppuNotBetween(Float value1, Float value2) {
            addCriterion("arppu not between", value1, value2, "arppu");
            return (Criteria) this;
        }

        public Criteria andNew7payIsNull() {
            addCriterion("new7pay is null");
            return (Criteria) this;
        }

        public Criteria andNew7payIsNotNull() {
            addCriterion("new7pay is not null");
            return (Criteria) this;
        }

        public Criteria andNew7payEqualTo(Float value) {
            addCriterion("new7pay =", value, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payNotEqualTo(Float value) {
            addCriterion("new7pay <>", value, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payGreaterThan(Float value) {
            addCriterion("new7pay >", value, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payGreaterThanOrEqualTo(Float value) {
            addCriterion("new7pay >=", value, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payLessThan(Float value) {
            addCriterion("new7pay <", value, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payLessThanOrEqualTo(Float value) {
            addCriterion("new7pay <=", value, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payIn(List<Float> values) {
            addCriterion("new7pay in", values, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payNotIn(List<Float> values) {
            addCriterion("new7pay not in", values, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payBetween(Float value1, Float value2) {
            addCriterion("new7pay between", value1, value2, "new7pay");
            return (Criteria) this;
        }

        public Criteria andNew7payNotBetween(Float value1, Float value2) {
            addCriterion("new7pay not between", value1, value2, "new7pay");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}