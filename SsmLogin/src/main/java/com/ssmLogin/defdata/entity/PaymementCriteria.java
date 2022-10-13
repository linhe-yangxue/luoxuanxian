package com.ssmLogin.defdata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class PaymementCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PaymementCriteria() {
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

        public Criteria andOwnOrderIsNull() {
            addCriterion("ownOrder is null");
            return (Criteria) this;
        }

        public Criteria andOwnOrderIsNotNull() {
            addCriterion("ownOrder is not null");
            return (Criteria) this;
        }

        public Criteria andOwnOrderEqualTo(String value) {
            addCriterion("ownOrder =", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderNotEqualTo(String value) {
            addCriterion("ownOrder <>", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderGreaterThan(String value) {
            addCriterion("ownOrder >", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderGreaterThanOrEqualTo(String value) {
            addCriterion("ownOrder >=", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderLessThan(String value) {
            addCriterion("ownOrder <", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderLessThanOrEqualTo(String value) {
            addCriterion("ownOrder <=", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderLike(String value) {
            addCriterion("ownOrder like", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderNotLike(String value) {
            addCriterion("ownOrder not like", value, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderIn(List<String> values) {
            addCriterion("ownOrder in", values, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderNotIn(List<String> values) {
            addCriterion("ownOrder not in", values, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderBetween(String value1, String value2) {
            addCriterion("ownOrder between", value1, value2, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andOwnOrderNotBetween(String value1, String value2) {
            addCriterion("ownOrder not between", value1, value2, "ownOrder");
            return (Criteria) this;
        }

        public Criteria andUuidIsNull() {
            addCriterion("uuid is null");
            return (Criteria) this;
        }

        public Criteria andUuidIsNotNull() {
            addCriterion("uuid is not null");
            return (Criteria) this;
        }

        public Criteria andUuidEqualTo(String value) {
            addCriterion("uuid =", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotEqualTo(String value) {
            addCriterion("uuid <>", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThan(String value) {
            addCriterion("uuid >", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThanOrEqualTo(String value) {
            addCriterion("uuid >=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThan(String value) {
            addCriterion("uuid <", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThanOrEqualTo(String value) {
            addCriterion("uuid <=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLike(String value) {
            addCriterion("uuid like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotLike(String value) {
            addCriterion("uuid not like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidIn(List<String> values) {
            addCriterion("uuid in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotIn(List<String> values) {
            addCriterion("uuid not in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidBetween(String value1, String value2) {
            addCriterion("uuid between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotBetween(String value1, String value2) {
            addCriterion("uuid not between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andGuidIsNull() {
            addCriterion("guid is null");
            return (Criteria) this;
        }

        public Criteria andGuidIsNotNull() {
            addCriterion("guid is not null");
            return (Criteria) this;
        }

        public Criteria andGuidEqualTo(Long value) {
            addCriterion("guid =", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotEqualTo(Long value) {
            addCriterion("guid <>", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThan(Long value) {
            addCriterion("guid >", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidGreaterThanOrEqualTo(Long value) {
            addCriterion("guid >=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThan(Long value) {
            addCriterion("guid <", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidLessThanOrEqualTo(Long value) {
            addCriterion("guid <=", value, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidIn(List<Long> values) {
            addCriterion("guid in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotIn(List<Long> values) {
            addCriterion("guid not in", values, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidBetween(Long value1, Long value2) {
            addCriterion("guid between", value1, value2, "guid");
            return (Criteria) this;
        }

        public Criteria andGuidNotBetween(Long value1, Long value2) {
            addCriterion("guid not between", value1, value2, "guid");
            return (Criteria) this;
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

        public Criteria andAccountIsNull() {
            addCriterion("account is null");
            return (Criteria) this;
        }

        public Criteria andAccountIsNotNull() {
            addCriterion("account is not null");
            return (Criteria) this;
        }

        public Criteria andAccountEqualTo(String value) {
            addCriterion("account =", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotEqualTo(String value) {
            addCriterion("account <>", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThan(String value) {
            addCriterion("account >", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThanOrEqualTo(String value) {
            addCriterion("account >=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThan(String value) {
            addCriterion("account <", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThanOrEqualTo(String value) {
            addCriterion("account <=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLike(String value) {
            addCriterion("account like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotLike(String value) {
            addCriterion("account not like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountIn(List<String> values) {
            addCriterion("account in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotIn(List<String> values) {
            addCriterion("account not in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountBetween(String value1, String value2) {
            addCriterion("account between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotBetween(String value1, String value2) {
            addCriterion("account not between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andNicknameIsNull() {
            addCriterion("nickname is null");
            return (Criteria) this;
        }

        public Criteria andNicknameIsNotNull() {
            addCriterion("nickname is not null");
            return (Criteria) this;
        }

        public Criteria andNicknameEqualTo(String value) {
            addCriterion("nickname =", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotEqualTo(String value) {
            addCriterion("nickname <>", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameGreaterThan(String value) {
            addCriterion("nickname >", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameGreaterThanOrEqualTo(String value) {
            addCriterion("nickname >=", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLessThan(String value) {
            addCriterion("nickname <", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLessThanOrEqualTo(String value) {
            addCriterion("nickname <=", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameLike(String value) {
            addCriterion("nickname like", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotLike(String value) {
            addCriterion("nickname not like", value, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameIn(List<String> values) {
            addCriterion("nickname in", values, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotIn(List<String> values) {
            addCriterion("nickname not in", values, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameBetween(String value1, String value2) {
            addCriterion("nickname between", value1, value2, "nickname");
            return (Criteria) this;
        }

        public Criteria andNicknameNotBetween(String value1, String value2) {
            addCriterion("nickname not between", value1, value2, "nickname");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("createDate is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("createDate is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("createDate =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("createDate <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("createDate >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("createDate >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("createDate <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("createDate <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("createDate in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("createDate not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("createDate between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("createDate not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateIsNull() {
            addCriterion("complateDate is null");
            return (Criteria) this;
        }

        public Criteria andComplateDateIsNotNull() {
            addCriterion("complateDate is not null");
            return (Criteria) this;
        }

        public Criteria andComplateDateEqualTo(Date value) {
            addCriterion("complateDate =", value, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateNotEqualTo(Date value) {
            addCriterion("complateDate <>", value, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateGreaterThan(Date value) {
            addCriterion("complateDate >", value, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("complateDate >=", value, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateLessThan(Date value) {
            addCriterion("complateDate <", value, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateLessThanOrEqualTo(Date value) {
            addCriterion("complateDate <=", value, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateIn(List<Date> values) {
            addCriterion("complateDate in", values, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateNotIn(List<Date> values) {
            addCriterion("complateDate not in", values, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateBetween(Date value1, Date value2) {
            addCriterion("complateDate between", value1, value2, "complateDate");
            return (Criteria) this;
        }

        public Criteria andComplateDateNotBetween(Date value1, Date value2) {
            addCriterion("complateDate not between", value1, value2, "complateDate");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderIsNull() {
            addCriterion("goodsOrder is null");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderIsNotNull() {
            addCriterion("goodsOrder is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderEqualTo(String value) {
            addCriterion("goodsOrder =", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderNotEqualTo(String value) {
            addCriterion("goodsOrder <>", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderGreaterThan(String value) {
            addCriterion("goodsOrder >", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderGreaterThanOrEqualTo(String value) {
            addCriterion("goodsOrder >=", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderLessThan(String value) {
            addCriterion("goodsOrder <", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderLessThanOrEqualTo(String value) {
            addCriterion("goodsOrder <=", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderLike(String value) {
            addCriterion("goodsOrder like", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderNotLike(String value) {
            addCriterion("goodsOrder not like", value, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderIn(List<String> values) {
            addCriterion("goodsOrder in", values, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderNotIn(List<String> values) {
            addCriterion("goodsOrder not in", values, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderBetween(String value1, String value2) {
            addCriterion("goodsOrder between", value1, value2, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andGoodsOrderNotBetween(String value1, String value2) {
            addCriterion("goodsOrder not between", value1, value2, "goodsOrder");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNull() {
            addCriterion("shopId is null");
            return (Criteria) this;
        }

        public Criteria andShopIdIsNotNull() {
            addCriterion("shopId is not null");
            return (Criteria) this;
        }

        public Criteria andShopIdEqualTo(Integer value) {
            addCriterion("shopId =", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotEqualTo(Integer value) {
            addCriterion("shopId <>", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThan(Integer value) {
            addCriterion("shopId >", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("shopId >=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThan(Integer value) {
            addCriterion("shopId <", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdLessThanOrEqualTo(Integer value) {
            addCriterion("shopId <=", value, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdIn(List<Integer> values) {
            addCriterion("shopId in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotIn(List<Integer> values) {
            addCriterion("shopId not in", values, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdBetween(Integer value1, Integer value2) {
            addCriterion("shopId between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andShopIdNotBetween(Integer value1, Integer value2) {
            addCriterion("shopId not between", value1, value2, "shopId");
            return (Criteria) this;
        }

        public Criteria andGoodsNumIsNull() {
            addCriterion("goodsNum is null");
            return (Criteria) this;
        }

        public Criteria andGoodsNumIsNotNull() {
            addCriterion("goodsNum is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsNumEqualTo(Integer value) {
            addCriterion("goodsNum =", value, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumNotEqualTo(Integer value) {
            addCriterion("goodsNum <>", value, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumGreaterThan(Integer value) {
            addCriterion("goodsNum >", value, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("goodsNum >=", value, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumLessThan(Integer value) {
            addCriterion("goodsNum <", value, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumLessThanOrEqualTo(Integer value) {
            addCriterion("goodsNum <=", value, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumIn(List<Integer> values) {
            addCriterion("goodsNum in", values, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumNotIn(List<Integer> values) {
            addCriterion("goodsNum not in", values, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumBetween(Integer value1, Integer value2) {
            addCriterion("goodsNum between", value1, value2, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andGoodsNumNotBetween(Integer value1, Integer value2) {
            addCriterion("goodsNum not between", value1, value2, "goodsNum");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("Amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("Amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Float value) {
            addCriterion("Amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Float value) {
            addCriterion("Amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Float value) {
            addCriterion("Amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Float value) {
            addCriterion("Amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Float value) {
            addCriterion("Amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Float value) {
            addCriterion("Amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Float> values) {
            addCriterion("Amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Float> values) {
            addCriterion("Amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Float value1, Float value2) {
            addCriterion("Amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Float value1, Float value2) {
            addCriterion("Amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andSendStatusIsNull() {
            addCriterion("sendStatus is null");
            return (Criteria) this;
        }

        public Criteria andSendStatusIsNotNull() {
            addCriterion("sendStatus is not null");
            return (Criteria) this;
        }

        public Criteria andSendStatusEqualTo(Integer value) {
            addCriterion("sendStatus =", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotEqualTo(Integer value) {
            addCriterion("sendStatus <>", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusGreaterThan(Integer value) {
            addCriterion("sendStatus >", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("sendStatus >=", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusLessThan(Integer value) {
            addCriterion("sendStatus <", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusLessThanOrEqualTo(Integer value) {
            addCriterion("sendStatus <=", value, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusIn(List<Integer> values) {
            addCriterion("sendStatus in", values, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotIn(List<Integer> values) {
            addCriterion("sendStatus not in", values, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusBetween(Integer value1, Integer value2) {
            addCriterion("sendStatus between", value1, value2, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("sendStatus not between", value1, value2, "sendStatus");
            return (Criteria) this;
        }

        public Criteria andSendTargetIsNull() {
            addCriterion("sendTarget is null");
            return (Criteria) this;
        }

        public Criteria andSendTargetIsNotNull() {
            addCriterion("sendTarget is not null");
            return (Criteria) this;
        }

        public Criteria andSendTargetEqualTo(String value) {
            addCriterion("sendTarget =", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetNotEqualTo(String value) {
            addCriterion("sendTarget <>", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetGreaterThan(String value) {
            addCriterion("sendTarget >", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetGreaterThanOrEqualTo(String value) {
            addCriterion("sendTarget >=", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetLessThan(String value) {
            addCriterion("sendTarget <", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetLessThanOrEqualTo(String value) {
            addCriterion("sendTarget <=", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetLike(String value) {
            addCriterion("sendTarget like", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetNotLike(String value) {
            addCriterion("sendTarget not like", value, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetIn(List<String> values) {
            addCriterion("sendTarget in", values, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetNotIn(List<String> values) {
            addCriterion("sendTarget not in", values, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetBetween(String value1, String value2) {
            addCriterion("sendTarget between", value1, value2, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andSendTargetNotBetween(String value1, String value2) {
            addCriterion("sendTarget not between", value1, value2, "sendTarget");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
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