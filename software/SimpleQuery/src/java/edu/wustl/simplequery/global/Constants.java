/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

package edu.wustl.simplequery.global;
/**
 * Constants required for SimpleQuery.
 * @author deepti_shelar
 *
 */
public class Constants
{
	public static final String FIELD_TYPE_BIGINT = "bigint";
	public static final String FIELD_TYPE_VARCHAR = "varchar";
	public static final String FIELD_TYPE_TEXT = "text";
	public static final String FIELD_TYPE_TINY_INT = "tinyint";
	public static final String NULL = "NULL";

	public static final String CONDITION_VALUE_YES = "yes";

	public static final String TINY_INT_VALUE_ONE = "1";
	public static final String TINY_INT_VALUE_ZERO = "0";
	public static final String PARENT_SPECIMEN_ID_COLUMN = "PARENT_SPECIMEN_ID";
	public static final int SIMPLE_QUERY_INTERFACE_ID = 40;
	public static final String SIMPLE_QUERY_MAP = "simpleQueryMap";
	public static final String SIMPLE_QUERY_ALIAS_NAME = "simpleQueryAliasName";

	public static final String SIMPLE_QUERY_INTERFACE_ACTION = "/SimpleQueryInterface.do";
	public static final String HYPERLINK_COLUMN_MAP = "hyperlinkColumnMap";
	
	public static final String ATTRIBUTE_NAME_LIST = "attributeNameList";
	public static final String ATTRIBUTE_CONDITION_LIST = "attributeConditionList";
	public static final String ORIGINAL_SIMPLE_QUERY_OBJECT = "originalSimpleQueryObject";
	public static final String SIMPLE_QUERY_COUNTER = "counter";
	// Query results view temporary table name.
	public static final String QUERY_RESULTS_TABLE = "CATISSUE_QUERY_RESULTS";
	public static final String SPREADSHEET_VIEW = "Spreadsheet View";
	
	
	public static final String CDE_NAME_TISSUE_SITE = "Tissue Site";
	public static final String[] ATTRIBUTE_CONDITION_ARRAY = {
        "=","<",">"        
};

	public static final String SELECT_OPTION = "-- Select --";
	 public static final String OPERATION = "operation";
	 public static final String REDEFINE = "redefine";	
	 public static final String ORIGINAL_SIMPLE_QUERY_COUNTER = "counter";
	 public static final String CONFIGURED_SELECT_COLUMN_LIST = "configuredSelectColumnList";
	 public static final String PAGEOF = "pageOf";
	 public static final int ONE = 1;
	 public static final int SIMPLE_QUERY_TABLES = 1;
	 public static final String OBJECT_NAME_LIST = "objectNameList";
	 public static final String[] ATTRIBUTE_NAME_ARRAY = {
	        SELECT_OPTION
	};
	
	public static final String IDENTIFIER = "IDENTIFIER";

	public static final String FIELD_TYPE_DATE = "date";
	public static final String FIELD_TYPE_TIMESTAMP_DATE = "timestampdate";
	public static final String FIELD_TYPE_TIMESTAMP_TIME = "timestamptime";
	public static final String UPPER = "UPPER";
	
	public static final String ROOT = "Root";
	
	public static final String ACTIVITY_STATUS_COLUMN = "ACTIVITY_STATUS";
	
	public static final String TABLE_ALIAS_NAME_COLUMN = "ALIAS_NAME";
	public static final String TABLE_DATA_TABLE_NAME = "CATISSUE_QUERY_TABLE_DATA";
	public static final String TABLE_DISPLAY_NAME_COLUMN = "DISPLAY_NAME";
	public static final String TABLE_FOR_SQI_COLUMN = "FOR_SQI";
	public static final String TABLE_ID_COLUMN = "TABLE_ID";
	
//	Constants for Summary Page
	public static final String TISSUE = "Tissue";
	public static final String MOLECULE = "Molecular";
	public static final String CELL = "Cell";
	public static final String FLUID = "Fluid";
	
	public static final String ANY = "Any";
	
	public static final String TABLE_ALIAS_NAME = "aliasName";
	public static final String AND_JOIN_CONDITION = "AND";
	public static final String REPORTED_PROBLEM_CLASS_NAME = "edu.wustl.catissuecore.domain.ReportedProblem";
	public static final String PAGEOF_SIMPLE_QUERY_INTERFACE = "pageOfSimpleQueryInterface";
	public static final String IDENTIFIER_FIELD_INDEX = "identifierFieldIndex";
	public static final String SIMPLE_QUERY_NO_RESULTS = "noResults";
	public static final String SEARCH_OBJECT_ACTION = "/SearchObject.do";
	public static final String SEARCH = "search";
	public static final String SIMPLE_QUERY_SINGLE_RESULT = "singleResult";
	public static final String SPREADSHEET_DATA_LIST = "spreadsheetDataList";
	public static final String SPREADSHEET_COLUMN_LIST = "spreadsheetColumnList";
	public static final String IS_SIMPLE_SEARCH = "isSimpleSearch";
}
