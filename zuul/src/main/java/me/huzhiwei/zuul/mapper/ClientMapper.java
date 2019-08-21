package me.huzhiwei.zuul.mapper;

import me.huzhiwei.zuul.domain.Client;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-21 16:32
 */
public interface ClientMapper {

    @Insert("insert into oauth_client_details(client_name, client_id, client_secret, resource_ids, scope, authorized_grant_types)" +
            "values(#{clientName}, #{clientId}, #{clientSecret}, #{resourceIds}, #{scope}, 'client_credentials')")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(Client client);

    @Update("update oauth_client_details set client_id = #{clientId}, client_secret = #{clientSecret} where id = #{id}")
    int update(Client client);

    @Select("select id, client_id clientId, client_secret clientSecret, resource_ids resourceIds, scope from oauth_client_details")
    List<Client> selectAll();

    @Delete("delete from oauth_client_details where id = #{id}")
    int delete(@Param("id") Long id);
}
