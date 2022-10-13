package com.ssmLogin.defdata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class GameplatCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GameplatCriteria() {
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

        public Criteria andPidNaIsNull() {
            addCriterion("pidNa is null");
            return (Criteria) this;
        }

        public Criteria andPidNaIsNotNull() {
            addCriterion("pidNa is not null");
            return (Criteria) this;
        }

        public Criteria andPidNaEqualTo(String value) {
            addCriterion("pidNa =", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaNotEqualTo(String value) {
            addCriterion("pidNa <>", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaGreaterThan(String value) {
            addCriterion("pidNa >", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaGreaterThanOrEqualTo(String value) {
            addCriterion("pidNa >=", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaLessThan(String value) {
            addCriterion("pidNa <", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaLessThanOrEqualTo(String value) {
            addCriterion("pidNa <=", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaLike(String value) {
            addCriterion("pidNa like", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaNotLike(String value) {
            addCriterion("pidNa not like", value, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaIn(List<String> values) {
            addCriterion("pidNa in", values, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaNotIn(List<String> values) {
            addCriterion("pidNa not in", values, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaBetween(String value1, String value2) {
            addCriterion("pidNa between", value1, value2, "pidNa");
            return (Criteria) this;
        }

        public Criteria andPidNaNotBetween(String value1, String value2) {
            addCriterion("pidNa not between", value1, value2, "pidNa");
            return (Criteria) this;
        }

        public Criteria andGameNaIsNull() {
            addCriterion("gameNa is null");
            return (Criteria) this;
        }

        public Criteria andGameNaIsNotNull() {
            addCriterion("gameNa is not null");
            return (Criteria) this;
        }

        public Criteria andGameNaEqualTo(String value) {
            addCriterion("gameNa =", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaNotEqualTo(String value) {
            addCriterion("gameNa <>", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaGreaterThan(String value) {
            addCriterion("gameNa >", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaGreaterThanOrEqualTo(String value) {
            addCriterion("gameNa >=", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaLessThan(String value) {
            addCriterion("gameNa <", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaLessThanOrEqualTo(String value) {
            addCriterion("gameNa <=", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaLike(String value) {
            addCriterion("gameNa like", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaNotLike(String value) {
            addCriterion("gameNa not like", value, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaIn(List<String> values) {
            addCriterion("gameNa in", values, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaNotIn(List<String> values) {
            addCriterion("gameNa not in", values, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaBetween(String value1, String value2) {
            addCriterion("gameNa between", value1, value2, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameNaNotBetween(String value1, String value2) {
            addCriterion("gameNa not between", value1, value2, "gameNa");
            return (Criteria) this;
        }

        public Criteria andGameOnlineIsNull() {
            addCriterion("gameOnline is null");
            return (Criteria) this;
        }

        public Criteria andGameOnlineIsNotNull() {
            addCriterion("gameOnline is not null");
            return (Criteria) this;
        }

        public Criteria andGameOnlineEqualTo(Date value) {
            addCriterionForJDBCDate("gameOnline =", value, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineNotEqualTo(Date value) {
            addCriterionForJDBCDate("gameOnline <>", value, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineGreaterThan(Date value) {
            addCriterionForJDBCDate("gameOnline >", value, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("gameOnline >=", value, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineLessThan(Date value) {
            addCriterionForJDBCDate("gameOnline <", value, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("gameOnline <=", value, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineIn(List<Date> values) {
            addCriterionForJDBCDate("gameOnline in", values, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineNotIn(List<Date> values) {
            addCriterionForJDBCDate("gameOnline not in", values, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("gameOnline between", value1, value2, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andGameOnlineNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("gameOnline not between", value1, value2, "gameOnline");
            return (Criteria) this;
        }

        public Criteria andIsRecordIsNull() {
            addCriterion("isRecord is null");
            return (Criteria) this;
        }

        public Criteria andIsRecordIsNotNull() {
            addCriterion("isRecord is not null");
            return (Criteria) this;
        }

        public Criteria andIsRecordEqualTo(Integer value) {
            addCriterion("isRecord =", value, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordNotEqualTo(Integer value) {
            addCriterion("isRecord <>", value, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordGreaterThan(Integer value) {
            addCriterion("isRecord >", value, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordGreaterThanOrEqualTo(Integer value) {
            addCriterion("isRecord >=", value, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordLessThan(Integer value) {
            addCriterion("isRecord <", value, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordLessThanOrEqualTo(Integer value) {
            addCriterion("isRecord <=", value, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordIn(List<Integer> values) {
            addCriterion("isRecord in", values, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordNotIn(List<Integer> values) {
            addCriterion("isRecord not in", values, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordBetween(Integer value1, Integer value2) {
            addCriterion("isRecord between", value1, value2, "isRecord");
            return (Criteria) this;
        }

        public Criteria andIsRecordNotBetween(Integer value1, Integer value2) {
            addCriterion("isRecord not between", value1, value2, "isRecord");
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