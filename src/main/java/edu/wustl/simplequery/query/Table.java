/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

/*
 * Created on Dec 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.simplequery.query;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;

import org.apache.struts.action.ActionErrors;

/**
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Table implements Serializable,edu.wustl.common.datatypes.IDBDataType
{

	private String tableName;
	private String tableAliasAppend;
	private Table linkingTable;

	/**
	 * @param tableName
	 * @param tableAliasAppend
	 */
	public Table(String tableName, String tableAliasAppend)
	{
		super();
		this.tableName = tableName;
		this.tableAliasAppend = tableAliasAppend;
	}

	public Table(String tableName, String tableAliasAppend, Table linkingTable)
	{
		super();
		this.tableName = tableName;
		this.tableAliasAppend = tableAliasAppend;
		this.linkingTable = linkingTable;
	}

	/**
	 * 
	 */
	public Table()
	{

	}

	/**
	 * @param tablename2
	 */
	public Table(String tablename2)
	{
		this.tableName = tablename2;
	}

	/**
	 * @param table
	 */
	public Table(Table table)
	{
		if (table != null)
		{
			this.tableName = table.tableName;
			this.tableAliasAppend = table.tableAliasAppend;
			if (linkingTable != null)
				this.linkingTable = new Table(linkingTable);
		}
	}

	/**
	 * @return Returns the tableAliasAppend.
	 */
	public String getTableAliasAppend()
	{
		return tableAliasAppend;
	}

	/**
	 * @param tableAliasAppend The tableAliasAppend to set.
	 */
	public void setTableAliasAppend(String tableAliasAppend)
	{
		this.tableAliasAppend = tableAliasAppend;
	}

	/**
	 * @return Returns the tableName.
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if (obj instanceof Table)
		{
			Table table = (Table) obj;
			if (!tableName.equals(table.tableName))
				return false;
			if (tableAliasAppend == null && table.tableAliasAppend == null)
			{
				return true;
			}
			if ((tableAliasAppend == null && table.tableName.equals(table.tableAliasAppend))
					|| (table.tableAliasAppend == null && tableName.equals(tableAliasAppend)))
			{
				return true;
			}
			if ((tableAliasAppend == null && !table.tableName.equals(table.tableAliasAppend))
					|| (table.tableAliasAppend == null && !tableName.equals(tableAliasAppend)))
			{
				return false;
			}
			if (!tableAliasAppend.equals(table.tableAliasAppend))
				return false;
			return true;
		}
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return 1;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public String toSQLString() throws SQLException
	{
		if (tableName == null)
		{
			throw new SQLException("table name is null");
		}
		else if (Client.objectTableNames.get(tableName) == null)
		{
			throw new SQLException("Unknown table name:" + tableName);
		}
		else if (tableAliasAppend != null)
		{
			return tableAliasAppend;
		}
		else
		{
			return tableName;
		}

	}

	/**
	 * @return
	 */
	public String toString()
	{
		return tableName + " " + tableAliasAppend;
	}

	/**
	 * @return
	 */
	public boolean hasDifferentAlias()
	{
		if (tableAliasAppend == null)
			return false;
		if (tableAliasAppend.equals(tableName))
			return false;
		else
			return true;
	}

	/**
	 * @return Returns the linkingTable.
	 */
	public Table getLinkingTable()
	{
		return linkingTable;
	}

	/**
	 * @param linkingTable The linkingTable to set.
	 */
	public void setLinkingTable(Table linkingTable)
	{
		this.linkingTable = linkingTable;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType
	 * #validate(java.lang.String, org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate entered values.
	 * @param enteredValue entered Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * get Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		return new Table(str, str);
	}
}