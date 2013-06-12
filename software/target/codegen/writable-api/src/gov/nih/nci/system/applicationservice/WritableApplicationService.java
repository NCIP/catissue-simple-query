/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-simple-query/LICENSE.txt for details.
 */

package gov.nih.nci.system.applicationservice;

import java.util.List;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;

public interface WritableApplicationService extends ApplicationService
{
	public SDKQueryResult executeQuery(SDKQuery query) throws ApplicationException;
	
	public List<SDKQueryResult> executeBatchQuery(List<SDKQuery> batchQuery) throws ApplicationException;
}