package me.huzhiwei.zuul.mapper;

import me.huzhiwei.zuul.domain.Client;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-21 16:32
 */
public interface ClientMapper {

    @Insert("insert into oauth_client_details(client_id, client_secret, resource_ids, scope, authorized_grant_types, available)" +
            "values(#{clientId}, #{clientSecret}, #{resourceIdsString}, #{scopeString}, 'client_credentials', #{available})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(Client client);

    int update(Client client);

    List<Client> selectListByClientId(@Param("clientId") String clientId);

    @Delete("delete from oauth_client_details where id = #{id}")
    int delete(@Param("id") Long id);

    @Select("select id, client_id clientId, client_secret clientSecret, resource_ids resourceIdsString, authorities authoritiesString, " +
            "scope scopeString, available, authorized_grant_types authorizedGrantTypesString " +
            "from oauth_client_details where id=#{id}")
    Client selectById(@Param("id") Long id);

    @Select("select id, client_id clientId, client_secret clientSecret, resource_ids resourceIdsString, authorities authoritiesString, " +
            "scope scopeString, available, authorized_grant_types authorizedGrantTypesString " +
            "from oauth_client_details where client_id=#{clientId} and available=1")
    Client selectByClientId(@Param("clientId") String clientId);
}
