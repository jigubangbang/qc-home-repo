package com.jigubangbang.com_service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;

@Mapper
public interface TravelmateMapper {
    //검색
    // @Select("SELECT id, name, continent FROM country ORDER BY name ASC")
    List<CountryDto> selectAllCountries();

    // @Select("SELECT id, city_name, country_id FROM city WHERE country_id = #{countryId} ORDER BY city_name ASC")
    List<CityDto> selectCitiesByCountryId(String countryId);
}
