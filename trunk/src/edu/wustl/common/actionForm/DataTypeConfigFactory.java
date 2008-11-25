/**
 *
 */

package edu.wustl.common.actionForm;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.ParseException;
import edu.wustl.common.util.global.XMLParserUtility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class configure factory for data types.
 * @author prashant_bandal
 *
 */
public final class DataTypeConfigFactory
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(DataTypeConfigFactory.class);

	/**
	 * Specifies ControlConfigurationsFactory instance.
	 */
	private static DataTypeConfigFactory dataTypeConfig = new DataTypeConfigFactory();

	/**
	 * Specifies dataType Configuration Map.
	 */
	private final Map<String, DataTypeConfigObject> dataTypeConfigurationMap;

	/**
	 * Specifies Document object.
	 */
	private Document dom;

	/**
	 * Specifies success.
	 */
	private static boolean success = true;

	/**
	 * ControlConfigurationsFactory constructor.
	 */
	private DataTypeConfigFactory()
	{
		dataTypeConfigurationMap = new HashMap<String, DataTypeConfigObject>();

		try
		{
			parseXML("DataTypeConfigurations.xml");
		}
		catch (ParseException exception)
		{
			success = false;
		}

	}

	/**
	 * This method gets ControlConfigurationsFactory Instance.
	 * @return ControlConfigurationsFactory instance.
	 * @throws ParseException Parse Exception.
	 */
	public static DataTypeConfigFactory getInstance() throws ParseException
	{
		if (success)
		{
			return dataTypeConfig;
		}
		else
		{
			throw new ParseException(null,null,"");
		}
	}

	/**
	 * This method parse xml File.
	 * @param xmlFile xml File
	 * @throws ParseException Parse Exception.
	 */
	private void parseXML(String xmlFile) throws ParseException
	{

		try
		{
			dom = XMLParserUtility.getDocument(xmlFile);
			parseDocument();
		}
		catch (Exception ioe)
		{
			logger.error(ioe.getMessage(), ioe);
			ErrorKey errorKey = null;
			throw new ParseException(errorKey,ioe,"");
		}
	}

	/**
	 * This method parse document.
	 */
	private void parseDocument()
	{
		//get the root elememt
		Element root = dom.getDocumentElement();

		NodeList validationDataTypeList = root.getElementsByTagName("DataType");
		//int noOfDataTypes = validationDataTypeList.getLength();
		Node validationDataTypeNode = null;

		//String dataTypeName = null;
		for (int i = 0; i < validationDataTypeList.getLength(); i++)
		{
			validationDataTypeNode = validationDataTypeList.item(i);
			if (validationDataTypeNode != null)
			{
				insertInToMap(validationDataTypeNode);
			}
		}
	}

	/**
	This method inserts dataType Name and DataTypeConfigObject into Map.
	 * @param validationDataTypeNode validation Data Type Node.
	 * @throws DOMException
	 */
	private void insertInToMap(Node validationDataTypeNode)
	{
		Node dataTypeNameNode;
		Node dataTypeClassNode;
		NamedNodeMap dataTypeAttributes;
		String dataTypeName = null;
		DataTypeConfigObject dataTypeConfigurationObject = new DataTypeConfigObject();
		dataTypeAttributes = validationDataTypeNode.getAttributes();
		if (dataTypeAttributes != null)
		{
			dataTypeNameNode = dataTypeAttributes.getNamedItem("name");
			dataTypeClassNode = dataTypeAttributes.getNamedItem("className");
			if (dataTypeNameNode != null && dataTypeClassNode != null)
			{
				dataTypeName = dataTypeNameNode.getNodeValue();
				dataTypeConfigurationObject.setDataTypeName(dataTypeName);
				dataTypeConfigurationObject.setDataTypeClassName(dataTypeClassNode.getNodeValue());
			}
		}
		dataTypeConfigurationMap.put(dataTypeName, dataTypeConfigurationObject);
	}

	/**
	 * This method gets Validator DataType object.
	 * @param dataType data Type
	 * @return dataTypeInterface
	 * @throws ParseException Parse Exception
	 */
	public IDBDataType getValidatorDataType(String dataType) throws ParseException
	{
		try
		{
			DataTypeConfigObject dataTypeConfig = dataTypeConfigurationMap.get(dataType);
			String className = dataTypeConfig.getClassName();
			Class<IDBDataType> dataTypeClass = (Class<IDBDataType>) Class.forName(className);
			return dataTypeClass.newInstance();
		}
		catch (Exception exception)
		{
			logger.error(exception.getMessage(), exception);
			ErrorKey errorKey = null;
			throw new ParseException(errorKey,exception,"");
		}
	}
}
