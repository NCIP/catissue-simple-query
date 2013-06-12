/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.simplequery.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Operator implements Serializable
{

	/**
	 * OR constant
	 */
	public static final String OR = "OR";

	/**
	 * EXIST constant
	 */
	public static final String EXIST = "EXIST";

	/**
	 * EQUAL constant
	 */
	public static final String EQUAL = "=";

	/**
	 * IN constant
	 */
	public static final String IN = "IN";

	/**
	 * NOT IN constant
	 */
	public static final String NOT_IN = "NOT IN";

	/**
	 * LESS THAN constant
	 */
	public static final String LESS_THAN = "Less than";

	/**
	 * AND constant
	 */
	public static final String AND = "AND";

	/**
	 * GREATER THAN constant
	 */
	public static final String GREATER_THAN = "Greater than";

	public static final String EQUALS_CONDITION = "Equals";

	public static final String NOT_EQUALS_CONDITION = "Not Equals";

	public static final String IN_CONDITION = "In";

	public static final String NOT_IN_CONDITION = "Not In";
	/**
	 * LIKE constant
	 */

	public static final String LIKE = "like";
	/**
	 * NOT EQUALS constant
	 */
	public static final String NOT_EQUALS = "!=";

	/**
	 * LESS THAN OR EQUALS constant
	 */
	public static final String LESS_THAN_OR_EQUALS = "Less than or Equal to";

	/**
	 * GREATER THAN OR EQUALS constant
	 */
	public static final String GREATER_THAN_OR_EQUALS = "Greater than or Equal to";

	/**
	 * BETWEEN constant
	 */
	public static final String BETWEEN = "Between";

	/**
	 * NOT BETWEEN constant
	 */
	public static final String NOT_BETWEEN = "Not Between";

	/**
	 * STARTS WITH constant
	 */
	public static final String STARTS_WITH = "Starts With";

	/**
	 * ENDS WITH constant
	 */
	public static final String ENDS_WITH = "Ends With";

	/**
	 * CONTAINS WITH constant
	 */
	public static final String CONTAINS = "Contains";

	public static final Map<Object,Object> operatorMap = new HashMap<Object,Object>()
	{{put(LESS_THAN,"<");
	  put(LESS_THAN_OR_EQUALS,"<=");
	  put(GREATER_THAN,">");
	  put(GREATER_THAN_OR_EQUALS,">=");
	}};

	/**
	 * Operator String
	 */
	private String operator;

	/**
	 * Additional parameters if any for an operator
	 */
	private String[] operatorParams;

	public Operator()
	{
	}

	public Operator(String operator)
	{
		this.operator = operator;
		if (operator.equals(Operator.EXIST))
		{
			operatorParams = new String[]{Operator.AND};
		}
		//START: Fix for Bug#1992
		else
		{
			operatorParams = new String[]{Operator.OR};
		}
		//END: Fix for Bug#1992
	}

	/**
	 * @param operator2
	 */
	public Operator(Operator operator2)
	{

		this.operator = operator2.operator;
		this.operatorParams = operator2.operatorParams;
	}

	public String toSQLString()
	{
		return " " + operator + " ";
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof DataElement)
		{
			Operator operator = (Operator) obj;
			if (!this.operator.equals(operator.operator))
				return false;
			return true;
		}
		else
			return false;
	}

	public String toString()
	{

		if (operatorParams != null && operatorParams.length > 0)
		{
			return this.operator + " Params" + this.operatorParams[0];
		}
		else
		{
			return this.operator + " Params:null";
		}
	}

	public int hashCode()
	{
		return 1;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	// Mandar : operators for null
	public static final String IS_NULL = "is Null";
	public static final String IS_NOT_NULL = "is Not Null";
	public static final String IS = "IS";
	//	public static final String NOT_NULL = "NOT NULL";
	public static final String IS_NOT = "IS NOT";

	/**
	 * @return Returns the operatorParams.
	 */
	public String[] getOperatorParams()
	{
		return operatorParams;
	}

	/**
	 * @param operatorParams The operatorParams to set.
	 */
	public void setOperatorParams(String[] operatorParams)
	{
		this.operatorParams = operatorParams;
	}
}
