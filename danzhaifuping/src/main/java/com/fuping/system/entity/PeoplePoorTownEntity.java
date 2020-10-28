package com.fuping.system.entity;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/20.
 * copyright easybiz.
 */

public class PeoplePoorTownEntity extends BaseEntity {
    public List<TownEntity> inspectionTownPoorerList;
    public List<TownEntity> inspectionTownVillageList;
    public List<PeopleEntity> inspectionPoorerList;
    public PeopleDetailEntity poor_each;
    public List<MenberEntity> familyInfoList;
    public PeopleDetailEntity inspectionPooerInfo_each;
    public List<SearchEntity> villageSearch_list;
    public List<CountryEntity> inspectionVillageList;
    public List<SearchEntity> poorSearch_list;
    public String recordCount = "0";
}
