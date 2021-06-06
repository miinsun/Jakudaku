package passionx3.jkdk.dao.mybatis.mapper;

/*
 *    Copyright 2010-2013 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */



import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import passionx3.jkdk.domain.Online;

@Mapper
public interface OnlineMapper {
	@Select("SELECT i.itemId AS itemId,  i.name AS name, i.price AS price, i.likeNum AS likeNum, i.thumbnail1 AS thumbnail1, "
			+ "o.totalRate AS totalRate, o.pcFile AS pcFile, o.tabletFile AS tabletFile, o.phoneFile AS phoneFile, o.saleState AS saleState "
			+ "FROM item i, onlineitem o "
			+ "WHERE i.itemId = o.itemId "
			+ "AND i.name LIKE '%' || #{keyword} || '%' "
			+ "AND i.isForSale = 1 "
			+ "AND i.approval = 1")
	List<Online> getOnlineItemsByKeyword(@Param("keyword") String keyword);
	
	@Select("SELECT i.itemId AS itemId, a.userId AS producerId, a.alias AS producerName, t.themeId AS themeId, t.name AS themeName, " +
			"i.name AS name, i.price AS price, i.likeNum AS likeNum, i.thumbnail1 AS thumbnail1, "+ 
			"i.thumbnail2 AS thumbnail2, i.thumbnail3 AS thumbnail3, i.isforsale AS isForSale, i.description AS description, c.categoryid AS categoryId, "+ 
			"c.name AS categoryName, o.totalRate AS totalRate, o.pcfile AS pcFile, o.tabletfile AS tabletFile, o.phonefile AS phoneFile, o.saleState AS saleState " + 
			"FROM item i, onlineitem o, account a, theme t, category c " + 
			"WHERE i.itemid = o.itemid AND i.userId = a.userId AND i.themeId = t.themeId AND o.categoryId = c.categoryId " + 
			"AND i.approval = 0 AND i.isForSale = 1")
	List<Online> getNotApprovedOnlineItems();

	@Update("UPDATE ITEM SET APPROVAL = -1 WHERE ITEMID = #{itemId}")
	int refuseItem(@Param("itemId") int itemId);

	@Update("UPDATE ITEM SET APPROVAL = 1 WHERE ITEMID = #{itemId}")
	int approveItem(@Param("itemId") int itemId);
	
	
	@Select("SELECT I.ITEMID, I.USERID AS PRODUCERID, I.NAME, I.UPLOADDATE, I.PRICE, I.LIKENUM, "
			+ "I.THUMBNAIL1, I.ISFORSALE, I.DESCRIPTION, I.APPROVAL, O.CATEGORYID, O.PCFILE, "
			+ "O.TABLETFILE, O.PHONEFILE, O.SALESTATE "
			+ "FROM ITEM I, ONLINEITEM O "
			+ "WHERE I.USERID = #{userId} AND I.ITEMID = O.ITEMID ORDER BY UPLOADDATE")
	List<Online> getOnlineItemListByProducerId(@Param("userId") String userId);

	@Select("SELECT itemID FROM (SELECT i.itemID AS itemID FROM item i, onlineitem o WHERE o.categoryID = #{categoryId} "
			+ "AND i.itemId = o.itemId AND i.isforsale = 1 AND i.approval = 1 order by dbms_random.value) WHERE ROWNUM < 3")
	List<Integer> getOnlineItemIdByCategoryforSale(@Param("categoryId") int categoryId);
	
	@Select("SELECT i.itemId AS itemId, a.userId AS producerId, a.alias AS producerName, t.themeId AS themeId, t.name AS themeName, \r\n" + 
			"i.name AS name, TO_DATE(i.uploadDate, 'YYYY-MM-DD') AS uploadDate, i.price AS price, i.likeNum AS likeNum, i.thumbnail1 AS thumbnail1,\r\n" + 
			"i.thumbnail2 AS thumbnail2, i.thumbnail3 AS thumbnail3, i.isforsale AS isForSale, i.description AS description, c.categoryid AS categoryId,\r\n" + 
			"c.name AS categoryName, o.totalRate AS totalRate, o.pcfile AS pcFile, o.tabletfile AS tabletFile, o.phonefile AS phoneFile, o.saleState AS saleState\r\n" + 
			"FROM item i, onlineitem o, account a, theme t, category c\r\n" + 
			"WHERE i.itemid = o.itemid AND i.userId = a.userId AND i.themeId = t.themeId AND o.categoryId = c.categoryId\r\n" + 
			"AND i.itemId = #{itemId} AND i.isForSale = 1")
	Online getOnlineItemById(@Param("itemId") int itemId); //효진 수정(상품 클릭해서 상품 상세 페이지로 넘어갈 때)


	@Select("SELECT itemID, name, uploaddate, price, totalrate, likenum, thumbnail1, categoryname, pcfile, tabletfile, phonefile, themeid, themename, userid FROM " + 
			"(SELECT i.itemId AS itemId, i.name AS name, uploaddate, price, o.totalrate AS totalrate, likenum, thumbnail1, isforsale, c.name AS categoryName, pcfile, tabletfile, phonefile, i.themeid as themeid, t.name as themename, userid " + 
			"FROM ONLINEITEM o, item i , category c, theme t WHERE o.itemid = i.itemid AND i.approval = 1 AND i.isforsale = 1 AND c.categoryId = o.categoryId AND i.themeId = t.themeId ORDER BY i.likenum DESC) WHERE ROWNUM < 6")
	List<Online> getBestOnlineItemListforHome();
	
	@Select("SELECT itemID, name, uploaddate, price, totalrate, likenum, thumbnail1, categoryname, pcfile, tabletfile, phonefile, themeid, themename, userid FROM " + 
			"(SELECT i.itemId AS itemId, i.name AS name, uploaddate, price, o.totalrate AS totalrate, likenum, thumbnail1, isforsale, c.name AS categoryName, pcfile, tabletfile, phonefile, i.themeid as themeid, t.name as themename, userid " + 
			"FROM ONLINEITEM o, item i , category c, theme t WHERE o.itemid = i.itemid AND i.approval = 1 AND i.isforsale = 1 AND c.categoryId = o.categoryId AND i.themeId = t.themeId ORDER BY i.uploaddate DESC) WHERE ROWNUM < 6")
	List<Online> getNewOnlineItemListforHome();

	@Select("SELECT I.ITEMID AS ITEMID, I.USERID AS PRODUCERID, A.ALIAS AS PRODUCERNAME, I.THEMEID AS THEMEID, T.NAME AS THEMENAME, I.NAME AS NAME, I.UPLOADDATE AS UPLOADDATE,"
			+ " I.PRICE AS PRICE, I.LIKENUM AS LIKENUM, I.THUMBNAIL1 AS THUMBNAIL1, I.THUMBNAIL2 AS THUMBNAIL2, I.THUMBNAIL3 AS THUMBNAIL3, I.ISFORSALE AS ISFORSALE, I.DESCRIPTION AS DESCRIPTION,"
			+ " I.APPROVAL AS APPROVAL, O.CATEGORYID AS CATEGORYID, C.NAME AS CATEGORYNAME, L.SALESTATE AS SALESTATE, O.TOTALRATE AS TOTALRATE, O.PCFILE AS PCFILE, O.TABLETFILE AS TABLETFILE, O.PHONEFILE AS PHONEFILE"
			+ " FROM ITEM I, ONLINEITEM O, LINEITEM L, ACCOUNT A, THEME T, CATEGORY C"
			+ " WHERE L.LINEITEMID = #{lineItemId} AND L.ITEMID = I.ITEMID"
			+ " AND I.ITEMID = O.ITEMID AND A.USERID = I.USERID AND T.THEMEID = I.THEMEID AND C.CATEGORYID = O.CATEGORYID")
	Online getOnlineItemByLineItemId(@Param("lineItemId") int lineItemId);

	@Insert("INSERT INTO onlineitem (itemid, totalrate, pcfile, tabletfile, phonefile, categoryid, salestate) "
			+ "values (#{onlineItem.itemId}, 0, #{onlineItem.pcFile}, #{onlineItem.tabletFile}, #{onlineItem.phoneFile}, "
				+ "#{onlineItem.categoryId}, 0)")
	int registerOnlineItem(@Param("onlineItem") Online onlineItem);
	
	@Update("UPDATE onlineitem "
			+ "SET pcfile = #{onlineItem.pcFile}, tabletfile = #{onlineItem.tabletFile}, phonefile = #{onlineItem.phoneFile}, categoryid = #{onlineItem.categoryId} "
			+ "WHERE ITEMID = #{onlineItem.itemId}")
	int updateOnlineItem(@Param("onlineItem") Online onlineItem);
	
	@Update("UPDATE onlineitem SET salestate = 0 WHERE itemid = #{itemId}")
	int updateOnlineItemSaleState(@Param("itemId") int itemId) throws DataAccessException;

	// 여기부터 조건 검색
	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, o.categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid"
			+ " AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.uploaddate DESC")
	List<Online> getOnlineItemListByCategoryOrderByUploadDate(int categoryId, String keyword);
  
	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, o.categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid AND i.approval = 1 AND i.isforsale = 1"
			+ " AND o.categoryid = #{categoryId} AND i.themeid = t.themeid"
			+ " AND i.themeId = #{themeId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.uploaddate DESC")
	List<Online> getOnlineItemListByThemeOrderByUploadDate(int categoryId, int themeId, String keyword);
	
	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid"
			+ " AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.likenum DESC")
	List<Online> getOnlineItemListByCategoryOrderByLikeNum(int categoryId, String keyword);

	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.themeId = #{themeId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.likenum DESC")
	List<Online> getOnlineItemListByThemeOrderByLikeNum(int categoryId, int themeId, String keyword);
	
	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid"
			+ " AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY o.totalrate DESC")
	List<Online> getOnlineItemListByCategoryOrderByTotalRate(int categoryId, String keyword);

	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.themeId = #{themeId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY o.totalrate DESC")
	List<Online> getOnlineItemListByThemeOrderByTotalRate(int categoryId, int themeId, String keyword);
	
	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid"
			+ " AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.price")
	List<Online> getOnlineItemListByCategoryOrderByPriceLow(int categoryId, String keyword);

	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.themeId = #{themeId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.price")
	List<Online> getOnlineItemListByThemeOrderByPriceLow(int categoryId, int themeId, String keyword);
	
	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid"
			+ " AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.price DESC")
	List<Online> getOnlineItemListByCategoryOrderByPriceHigh(int categoryId, String keyword);

	@Select("SELECT i.itemId AS itemId, i.name, uploaddate, price, likenum, thumbnail1, isforsale, categoryid, t.name as themeName, description, i.themeid, userid, pcFile, tabletFile, phoneFile, totalrate, salestate"
			+ " FROM ONLINEITEM o, item i, theme t"
			+ " WHERE o.itemid = i.itemid AND i.approval = 1 AND i.isforsale = 1"
			+ " AND i.themeid = t.themeid"
			+ " AND o.categoryid = #{categoryId} AND i.themeId = #{themeId} AND i.name LIKE '%' ||  #{keyword} || '%'"
			+ " ORDER BY i.price DESC")
	List<Online> getOnlineItemListByThemeOrderByPriceHigh(int categoryId, int themeId, String keyword);

	@Insert("UPDATE onlineitem SET salestate = #{state} where itemid = #{itemId}")
	void updateSaleState(int state, int itemId);

	@Insert("UPDATE item SET uploaddate = #{today} where itemid = #{itemId}")
	void updateUploadDate(int itemId, String today);
}
