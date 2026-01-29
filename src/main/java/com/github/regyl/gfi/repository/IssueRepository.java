package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.entity.IssueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IssueRepository {

    void saveAll(@Param("entities") List<IssueEntity> entities, @Param("tableName") String tableName);

    @Select("""
<script>
SELECT i.url 
FROM gfi.issue_v i
JOIN gfi.repository_v r ON i.repository_id = r.id
<where>
    <if test="filters.languages != null and !filters.languages.isEmpty()">
        AND r.language IN 
        <foreach item="lang" collection="filters.languages" open="(" separator="," close=")">
            #{lang}
        </foreach>
    </if>
    <if test="filters.filter != null">
        
        /* Φίλτρο για Stars με βάση τον Operator */
        <if test="filters.filter.stars != null and filters.filter.stars.value != null">
            <choose>
                <when test="filters.filter.stars.operator.name() == 'GREATER'"> AND r.stars &gt; #{filters.filter.stars.value} </when>
                <when test="filters.filter.stars.operator.name() == 'GREATER_OR_EQUAL'"> AND r.stars &gt;= #{filters.filter.stars.value} </when>
                <when test="filters.filter.stars.operator.name() == 'LESS'"> AND r.stars &lt; #{filters.filter.stars.value} </when>
                <when test="filters.filter.stars.operator.name() == 'LESS_OR_EQUAL'"> AND r.stars &lt;= #{filters.filter.stars.value} </when>
                <when test="filters.filter.stars.operator.name() == 'EQUALS'"> AND r.stars = #{filters.filter.stars.value} </when>
                <when test="filters.filter.stars.operator.name() == 'NOT_EQUALS'"> AND r.stars != #{filters.filter.stars.value} </when>
            </choose>
        </if>

        /* Φίλτρο για Γλώσσες μέσα στο FilterRequestDto */
        <if test="filters.filter.languages != null and filters.filter.languages.values != null and !filters.filter.languages.values.isEmpty()">
            <choose>
                <when test="filters.filter.languages.operator.name() == 'IN'">
                    AND r.language IN 
                    <foreach item="val" collection="filters.filter.languages.values" open="(" separator="," close=")">
                        #{val}
                    </foreach>
                </when>
                <when test="filters.filter.languages.operator.name() == 'NOT_IN'">
                    AND r.language NOT IN 
                    <foreach item="val" collection="filters.filter.languages.values" open="(" separator="," close=")">
                        #{val}
                    </foreach>
                </when>
            </choose>
        </if>
    </if>
</where>
ORDER BY RANDOM() LIMIT 1
</script>
""")
    String findRandomIssueLink(@Param("filters") DataRequestDto filters);
}
