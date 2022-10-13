package com.ssmLogin.defdata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class AcodedateCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AcodedateCriteria() {
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

        public Criteria andSignIsNull() {
            addCriterion("sign is null");
            return (Criteria) this;
        }

        public Criteria andSignIsNotNull() {
            addCriterion("sign is not null");
            return (Criteria) this;
        }

        public Criteria andSignEqualTo(String value) {
            addCriterion("sign =", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotEqualTo(String value) {
            addCriterion("sign <>", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignGreaterThan(String value) {
            addCriterion("sign >", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignGreaterThanOrEqualTo(String value) {
            addCriterion("sign >=", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignLessThan(String value) {
            addCriterion("sign <", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignLessThanOrEqualTo(String value) {
            addCriterion("sign <=", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignLike(String value) {
            addCriterion("sign like", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotLike(String value) {
            addCriterion("sign not like", value, "sign");
            return (Criteria) this;
        }

        public Criteria andSignIn(List<String> values) {
            addCriterion("sign in", values, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotIn(List<String> values) {
            addCriterion("sign not in", values, "sign");
            return (Criteria) this;
        }

        public Criteria andSignBetween(String value1, String value2) {
            addCriterion("sign between", value1, value2, "sign");
            return (Criteria) this;
        }

        public Criteria andSignNotBetween(String value1, String value2) {
            addCriterion("sign not between", value1, value2, "sign");
            return (Criteria) this;
        }

        public Criteria andProductDateIsNull() {
            addCriterion("productDate is null");
            return (Criteria) this;
        }

        public Criteria andProductDateIsNotNull() {
            addCriterion("productDate is not null");
            return (Criteria) this;
        }

        public Criteria andProductDateEqualTo(Date value) {
            addCriterion("productDate =", value, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateNotEqualTo(Date value) {
            addCriterion("productDate <>", value, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateGreaterThan(Date value) {
            addCriterion("productDate >", value, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateGreaterThanOrEqualTo(Date value) {
            addCriterion("productDate >=", value, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateLessThan(Date value) {
            addCriterion("productDate <", value, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateLessThanOrEqualTo(Date value) {
            addCriterion("productDate <=", value, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateIn(List<Date> values) {
            addCriterion("productDate in", values, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateNotIn(List<Date> values) {
            addCriterion("productDate not in", values, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateBetween(Date value1, Date value2) {
            addCriterion("productDate between", value1, value2, "productDate");
            return (Criteria) this;
        }

        public Criteria andProductDateNotBetween(Date value1, Date value2) {
            addCriterion("productDate not between", value1, value2, "productDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateIsNull() {
            addCriterion("generateDate is null");
            return (Criteria) this;
        }

        public Criteria andGenerateDateIsNotNull() {
            addCriterion("generateDate is not null");
            return (Criteria) this;
        }

        public Criteria andGenerateDateEqualTo(Long value) {
            addCriterion("generateDate =", value, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateNotEqualTo(Long value) {
            addCriterion("generateDate <>", value, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateGreaterThan(Long value) {
            addCriterion("generateDate >", value, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateGreaterThanOrEqualTo(Long value) {
            addCriterion("generateDate >=", value, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateLessThan(Long value) {
            addCriterion("generateDate <", value, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateLessThanOrEqualTo(Long value) {
            addCriterion("generateDate <=", value, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateIn(List<Long> values) {
            addCriterion("generateDate in", values, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateNotIn(List<Long> values) {
            addCriterion("generateDate not in", values, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateBetween(Long value1, Long value2) {
            addCriterion("generateDate between", value1, value2, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateDateNotBetween(Long value1, Long value2) {
            addCriterion("generateDate not between", value1, value2, "generateDate");
            return (Criteria) this;
        }

        public Criteria andGenerateNumIsNull() {
            addCriterion("generateNum is null");
            return (Criteria) this;
        }

        public Criteria andGenerateNumIsNotNull() {
            addCriterion("generateNum is not null");
            return (Criteria) this;
        }

        public Criteria andGenerateNumEqualTo(Integer value) {
            addCriterion("generateNum =", value, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumNotEqualTo(Integer value) {
            addCriterion("generateNum <>", value, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumGreaterThan(Integer value) {
            addCriterion("generateNum >", value, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("generateNum >=", value, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumLessThan(Integer value) {
            addCriterion("generateNum <", value, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumLessThanOrEqualTo(Integer value) {
            addCriterion("generateNum <=", value, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumIn(List<Integer> values) {
            addCriterion("generateNum in", values, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumNotIn(List<Integer> values) {
            addCriterion("generateNum not in", values, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumBetween(Integer value1, Integer value2) {
            addCriterion("generateNum between", value1, value2, "generateNum");
            return (Criteria) this;
        }

        public Criteria andGenerateNumNotBetween(Integer value1, Integer value2) {
            addCriterion("generateNum not between", value1, value2, "generateNum");
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