package bsuir.server.dao.impl;

import bsuir.server.dao.ApplianceDAO;
import bsuir.server.entity.Factory;
import bsuir.server.entity.criteria.Criteria;
import bsuir.server.entity.info.Info;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApplianceDAOImpl implements ApplianceDAO {

	private final Factory infoFactory = new Factory();

	private static String getTagValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = nodeList.item(0);
		return node.getNodeValue();
	}

	public List<Info> getAll(String path, String type){
		File xmlFile = new File("src/main/resources/students.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			Node node = document.getDocumentElement();
			List<Info> students = new ArrayList<>();
			node = node.getChildNodes().item(1);

			while (node!=null){
				if (!node.getNodeName().equals("#text")) {
					NodeList nodeList = node.getChildNodes();
					ArrayList<String> parameters = new ArrayList<String>();
					for (int i = 0; i < nodeList.getLength(); i++) {
						if (!nodeList.item(i).getNodeName().equals("#text")) {
							parameters.add(getTagValue(nodeList.item(i).getNodeName(),(Element)node));
						}
					}
					students.add(infoFactory.getInfo(parameters, type));
				}
				node = node.getNextSibling();
			}
			return students;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}

	public boolean add(List<String[]> parameters, String path, String type) throws ParserConfigurationException, IOException, SAXException, TransformerException {
		try {
			DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = newDocumentBuilder.parse(path);
			Element clientElement = doc.createElement(type);
			Element root = doc.getDocumentElement();
			for (String[] parameter : parameters) {
				System.out.println(parameter[0]);
				Element element = doc.createElement(parameter[0]);
				element.setTextContent(parameter[1]);
				clientElement.appendChild(element);
			}
			root.appendChild(clientElement);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Source source = new DOMSource(doc);
			Result result = new StreamResult(path);
			transformer.transform(source, result);
			return true;
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean edit(String name, List<String[]> parameters, String path) {File xmlFile = new File(path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			Node node = document.getDocumentElement();
			node = node.getChildNodes().item(1);
			while (node!=null){
				if (!node.getNodeName().equals("#text")) {
					NodeList nodeList = node.getChildNodes();
					if (nodeList.item(1).getTextContent().equals(name)) {
						int k = 0;
						for (int i = 0; i < nodeList.getLength(); i++) {
							if (!nodeList.item(i).getNodeName().equals("#text")) {
								nodeList.item(i).setTextContent(parameters.get(k)[1]);
								k++;
							}
						}
					}
				}
				node = node.getNextSibling();
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Source source = new DOMSource(document);
			Result result = new StreamResult(path);
			transformer.transform (source, result);
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return false;
	}

	public List<Info> get(Criteria criteria, String path, String type) {
		File xmlFile = new File(path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			Node node = document.getDocumentElement();
			List<Info> infoList = new ArrayList<Info>();
			node = node.getChildNodes().item(1);
			while (node!=null){
				if (!node.getNodeName().equals("#text")) {
					NodeList nodeList = node.getChildNodes();
					ArrayList<String> parameters = new ArrayList<String>();
					ArrayList<String> parametersInfo = new ArrayList<>();
					for (int i = 0; i < nodeList.getLength(); i++) {
						if (!nodeList.item(i).getNodeName().equals("#text")) {
							parameters.add(getTagValue(nodeList.item(i).getNodeName(),(Element)node));
							parametersInfo.add(nodeList.item(i).getNodeName());
						}
					}
					Set<String> applianceProperties = criteria.getCriteria().keySet();
					if (!applianceProperties.isEmpty()) {
						boolean isCriteria = true;
						for (String property : applianceProperties) {
							int index = parametersInfo.indexOf(property);
							System.out.println(criteria.getCriteria().get(property).toString());
							System.out.println(parameters.get(index));
							if ((index != -1) && (!criteria.getCriteria().get(property).toString().equals(parameters.get(index)))) {
								isCriteria = false;
							}
							if (isCriteria) {
								infoList.add(infoFactory.getInfo(parameters, type));
							}
						}
					}
					else{
						infoList.add(infoFactory.getInfo(parameters, type));
					}
				}
				node = node.getNextSibling();
			}
			return infoList;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}

}
