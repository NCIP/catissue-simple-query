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
 * <p>Title: QueryTreeNodeMap Class>
 * <p>Description:  QueryTreeNodeMap Class is used map the name of the node according 
 * to its level in the query tree.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.simplequery.query;

import java.util.HashMap;
import java.util.Map;

/**
 * QueryTreeNodeMap Class is used map the name of the node according 
 * to its level in the query tree.
 * @author gautam_shetty
 */
public class QueryTreeNodeMap
{

	/**
	 * Map of name of node to the level.
	 */
	private Map nodeMap = new HashMap();

	/**
	 * Initializes a QueryTreeNodeMap. 
	 */
	public QueryTreeNodeMap()
	{
		nodeMap.put(Integer.valueOf(0), "Root");
		nodeMap.put(Integer.valueOf(1), "Participant");
		nodeMap.put(Integer.valueOf(2), "Accession");
		nodeMap.put(Integer.valueOf(3), "Specimen");
		nodeMap.put(Integer.valueOf(4), "Segment");
		nodeMap.put(Integer.valueOf(5), "Sample");
	}

	/**
	 * Returns the name of the node according to the level.
	 * @param level The level of the node.
	 * @return the name of the node according to the level.
	 */
	public String getNodeName(int level)
	{
		return (String) nodeMap.get(Integer.valueOf(level));
	}
}
