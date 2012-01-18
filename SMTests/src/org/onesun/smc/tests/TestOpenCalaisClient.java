/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.onesun.smc.tests;

import java.io.IOException;

import org.onesun.commons.Log4jUtils;
import org.onesun.commons.text.classification.opencalais.OpenCalaisClient;

public class TestOpenCalaisClient {
	public static void main(String[] args) throws IOException{
		Log4jUtils.initLog();

		OpenCalaisClient client = new OpenCalaisClient();
		// *******************************************************************
		// DO NOT FORGET TO SET YOUR OWN KEY HERE BEFORE RUNNING APP
		// You can get a key from: http://www.opencalais.com/APIkey
		// *******************************************************************
		OpenCalaisClient.setLicenseKey("4565wkbnyckge3ztxcwhbrfr");
		// *******************************************************************
		
		String output = client.execute("Michael Joseph Jackson[1] (August 29, 1958 � June 25, 2009) was an American recording artist, " +
				"entertainer, and businessman. Referred to as the King of Pop, or by his initials MJ,[2] Jackson is recognized as the most " +
				"successful entertainer of all time by Guinness World Records. His contribution to music, dance, and fashion, along with a " +
				"much-publicized personal life, made him a global figure in popular culture for over four decades. The seventh child of the " +
				"Jackson family, he debuted on the professional music scene along with his brothers as a member of The Jackson 5, " +
				"then the Jacksons in 1964, and began his solo career in 1971.In the early 1980s, Jackson became a dominant figure in popular" +
				" music. The music videos for his songs, including those of \"Beat It\", \"Billie Jean\", and \"Thriller\", were credited with" +
				" transforming the medium into an art form and a promotional tool, and the popularity of these videos helped to bring " +
				"the relatively new television channel MTV to fame. Videos such as \"Black or White\" and \"Scream\" made him a staple on" +
				" MTV in the 1990s. Through stage performances and music videos, Jackson popularized a number of dance techniques, " +
				"such as the robot and the moonwalk, to which he gave the name. His distinctive musical sound and vocal style have " +
				"influenced numerous hip hop, pop, contemporary R&B, and rock artists.Jackson's 1982 album Thriller is the best-selling " +
				"album of all time. His other records, including Off the Wall (1979), Bad (1987), Dangerous (1991), and HIStory (1995), " +
				"also rank among the world's best-selling. Jackson is one of the few artists to have been inducted into the Rock and Roll " +
				"Hall of Fame twice. He was also inducted into the Dance Hall of Fame as the first (and currently only) dancer from the world " +
				"of pop and rock 'n' roll. Some of his other achievements include multiple Guinness World Records; 13 Grammy Awards (as well " +
				"as the Grammy Legend Award and the Grammy Lifetime Achievement Award); 26 American Music Awards (more than any other artist, " +
				"including the \"Artist of the Century\"); 13 number-one singles in the United States in his solo career and the estimated " +
				"sale of over 750 million records worldwide. Jackson won hundreds of awards, which have made him the most-awarded recording " +
				"artist in the history of popular music.[3]");

		System.out.println(output);

		// Sample output generated for the above text
		/*
			<!--Use of the Calais Web Service is governed by the Terms of Service located at http://www.opencalais.com. By using this service or the results of the service you agree to these terms of service.--><!--Relations: GenericRelations

			Company: MTV
			Country: United States
			EntertainmentAwardEvent: 13 Grammy Awards, 26 American Music Awards, the Grammy, the Grammy Lifetime Achievement Award
			IndustryTerm: promotional tool
			MusicAlbum: Black or White, Scream, Thriller
			Organization: The Jackson
			Person: Michael Joseph Jackson
			Position: Artist, King, artist, businessman, dancer, recording artist-->
			
			<?xml version="1.0" encoding="UTF-8"?>
			<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:c="http://s.opencalais.com/1/pred/">
			   <rdf:Description c:calaisRequestID="bfc4b99c-4446-5295-133c-352576743252" c:id="http://id.opencalais.com/qr55gE6l5tB7Ph*IyX34cA" rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/DocInfo" />
			      <c:document><![CDATA[Michael Joseph Jackson[1] (August 29, 1958 ? June 25, 2009) was an American recording artist, entertainer, and businessman. Referred to as the King of Pop, or by his initials MJ,[2] Jackson is recognized as the most successful entertainer of all time by Guinness World Records. His contribution to music, dance, and fashion, along with a much-publicized personal life, made him a global figure in popular culture for over four decades. The seventh child of the Jackson family, he debuted on the professional music scene along with his brothers as a member of The Jackson 5, then the Jacksons in 1964, and began his solo career in 1971.In the early 1980s, Jackson became a dominant figure in popular music. The music videos for his songs, including those of "Beat It", "Billie Jean", and "Thriller", were credited with transforming the medium into an art form and a promotional tool, and the popularity of these videos helped to bring the relatively new television channel MTV to fame. Videos such as "Black or White" and "Scream" made him a staple on MTV in the 1990s. Through stage performances and music videos, Jackson popularized a number of dance techniques, such as the robot and the moonwalk, to which he gave the name. His distinctive musical sound and vocal style have influenced numerous hip hop, pop, contemporary R&B, and rock artists.Jackson's 1982 album Thriller is the best-selling album of all time. His other records, including Off the Wall (1979), Bad (1987), Dangerous (1991), and HIStory (1995), also rank among the world's best-selling. Jackson is one of the few artists to have been inducted into the Rock and Roll Hall of Fame twice. He was also inducted into the Dance Hall of Fame as the first (and currently only) dancer from the world of pop and rock 'n' roll. Some of his other achievements include multiple Guinness World Records; 13 Grammy Awards (as well as the Grammy Legend Award and the Grammy Lifetime Achievement Award); 26 American Music Awards (more than any other artist, including the "Artist of the Century"); 13 number-one singles in the United States in his solo career and the estimated sale of over 750 million records worldwide. Jackson won hundreds of awards, which have made him the most-awarded recording artist in the history of popular music.[3]]]></c:document>
			      <c:docTitle />
			      <c:docDate>2011-11-20 17:31:53.927</c:docDate>
			   </rdf:Description>
			   <rdf:Description c:contentType="text/raw" c:emVer="7.1.1103.5" c:langIdVer="DefaultLangId" c:language="English" c:processingVer="CalaisJob01" c:stagsVer="1.0.0-b1-2009-11-12_16:54:24" c:submissionDate="2011-11-20 17:31:53.319" rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/meta">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/DocInfoMeta" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:submitterCode>6324432c-3ed3-1659-9751-a386806a4c1a</c:submitterCode>
			      <c:signature>digestalg-1|M9XeapR1qlzslZw8zj3wqlfRAd0=|FAi7LbFWN3DjdDPhG+yAO83O1vRcRq5jakeFWa0QWDG57aHqGBfVCA==</c:signature>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/lid/DefaultLangId">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/lid/DefaultLangId" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:lang rdf:resource="http://d.opencalais.com/lid/DefaultLangId/English" />
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/1">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/717004c7-f1f3-377e-9fc8-06323551148d" />
			      <c:name>Music</c:name>
			      <c:importance>1</c:importance>
			      <c:originalValue>Music</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/2">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/3e1a2397-31f9-34c6-99fe-dacf637ef357" />
			      <c:name>American music</c:name>
			      <c:importance>1</c:importance>
			      <c:originalValue>American music</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/3">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/8feb37fb-c0da-33dc-a744-10c15ec17464" />
			      <c:name>Michael Jackson</c:name>
			      <c:importance>1</c:importance>
			      <c:originalValue>Michael Jackson</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/4">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/d521cd61-5d8f-3603-931a-521dae42f2e8" />
			      <c:name>Billie Jean</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>Billie Jean</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/5">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/eb551d75-ff1d-3bf3-8202-3fb1ca7beb38" />
			      <c:name>The Jackson 5</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>The Jackson 5</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/6">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/8ec4be68-6e27-3626-928a-07c1969ff164" />
			      <c:name>Thriller</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>Thriller (album)</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/7">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/9d479708-9558-312e-af9b-01ab1f063eb7" />
			      <c:name>Beat It</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>Beat It</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/8">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/39459502-1a3f-36d4-9654-2276982cf106" />
			      <c:name>Bad</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>Bad (album)</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/9">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/f4178343-1f68-32a5-a6fa-d099cbd675de" />
			      <c:name>Off the Wall</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>Off the Wall (album)</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/10">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/c225e032-db50-305e-a1ef-39c5c209086d" />
			      <c:name>Scream</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>Scream (song)</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/11">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/74376556-1483-3630-8b0e-58f94b5a44ef" />
			      <c:name>MTV</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>MTV</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/12">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/98709273-865c-3a67-baa2-e7b3c979d114" />
			      <c:name>Jackson family</c:name>
			      <c:importance>2</c:importance>
			      <c:originalValue>Jackson family</c:originalValue>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/cat/1">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/cat/DocCat" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:category rdf:resource="http://d.opencalais.com/cat/Calais/EntertainmentCulture" />
			      <c:classifierName>Calais</c:classifierName>
			      <c:categoryName>Entertainment_Culture</c:categoryName>
			      <c:score>1.000</c:score>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/13">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/68a5e7ae-0246-3aed-a1fb-a22ca7e9ac3c" />
			      <!--categorization classifier: Calais-->
			      <c:name>Entertainment_Culture</c:name>
			      <c:importance>1</c:importance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/cat/2">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/cat/DocCat" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:category rdf:resource="http://d.opencalais.com/cat/Calais/HumanInterest" />
			      <c:classifierName>Calais</c:classifierName>
			      <c:categoryName>Human Interest</c:categoryName>
			      <c:score>1.000</c:score>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/SocialTag/14">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/tag/SocialTag" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:socialtag rdf:resource="http://d.opencalais.com/genericHasher-1/437fce1a-70dd-305a-b936-6d4478cc93a3" />
			      <!--categorization classifier: Calais-->
			      <c:name>Human Interest</c:name>
			      <c:importance>1</c:importance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/acf514cf-ef3d-35b8-b23b-3f0b14c682ce">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <!--Michael Joseph Jackson-->
			      <c:relationobject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <c:verb>induct</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/1">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/acf514cf-ef3d-35b8-b23b-3f0b14c682ce" />
			      <!--GenericRelations: relationobject: Michael Joseph Jackson; verb: induct; -->
			      <c:detection>[into the Rock and Roll Hall of Fame twice. ]He was also inducted into the Dance Hall of Fame[ as the first (and currently only) dancer from]</c:detection>
			      <c:prefix>into the Rock and Roll Hall of Fame twice.</c:prefix>
			      <c:exact>He was also inducted into the Dance Hall of Fame</c:exact>
			      <c:suffix>as the first (and currently only) dancer from</c:suffix>
			      <c:offset>1657</c:offset>
			      <c:length>48</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/5424d271-2109-31b0-9a61-2fd8285b023b">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/MusicAlbum" />
			      <c:name>Black or White</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/2">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/5424d271-2109-31b0-9a61-2fd8285b023b" />
			      <!--MusicAlbum: Black or White; -->
			      <c:detection>[television channel MTV to fame. Videos such as ]"Black or White"[ and "Scream" made him a staple on MTV in the]</c:detection>
			      <c:prefix>television channel MTV to fame. Videos such as</c:prefix>
			      <c:exact>"Black or White"</c:exact>
			      <c:suffix>and "Scream" made him a staple on MTV in the</c:suffix>
			      <c:offset>1000</c:offset>
			      <c:length>16</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/1">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/5424d271-2109-31b0-9a61-2fd8285b023b" />
			      <c:relevance>0.204</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/611d2032-8f27-34cc-b964-0b63737c4373">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <!--Michael Joseph Jackson-->
			      <c:relationsubject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <c:verb>debut</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/3">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/611d2032-8f27-34cc-b964-0b63737c4373" />
			      <!--GenericRelations: relationsubject: Michael Joseph Jackson; verb: debut; -->
			      <c:detection>[The seventh child of the Jackson family, ]he debuted on the professional music scene[ along with his brothers as a member of The]</c:detection>
			      <c:prefix>The seventh child of the Jackson family,</c:prefix>
			      <c:exact>he debuted on the professional music scene</c:exact>
			      <c:suffix>along with his brothers as a member of The</c:suffix>
			      <c:offset>477</c:offset>
			      <c:length>42</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/e98a7d1b-fe92-3409-8082-396602fdc070">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Position" />
			      <c:name>King</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/4">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/e98a7d1b-fe92-3409-8082-396602fdc070" />
			      <!--Position: King; -->
			      <c:detection>[entertainer, and businessman. Referred to as ]the King[ of Pop, or by his initials MJ,[2] Jackson is]</c:detection>
			      <c:prefix>entertainer, and businessman. Referred to as</c:prefix>
			      <c:exact>the King</c:exact>
			      <c:suffix>of Pop, or by his initials MJ,[2] Jackson is</c:suffix>
			      <c:offset>139</c:offset>
			      <c:length>8</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/2">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/e98a7d1b-fe92-3409-8082-396602fdc070" />
			      <c:relevance>0.314</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/1a06dfe2-b5fb-39a7-a8d1-c3f1a1018870">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/EntertainmentAwardEvent" />
			      <c:name>the Grammy</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/5">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/1a06dfe2-b5fb-39a7-a8d1-c3f1a1018870" />
			      <!--EntertainmentAwardEvent: the Grammy; -->
			      <c:detection>[World Records; 13 Grammy Awards (as well as ]the Grammy[ Legend Award and the Grammy Lifetime Achievement]</c:detection>
			      <c:prefix>World Records; 13 Grammy Awards (as well as</c:prefix>
			      <c:exact>the Grammy</c:exact>
			      <c:suffix>Legend Award and the Grammy Lifetime Achievement</c:suffix>
			      <c:offset>1889</c:offset>
			      <c:length>10</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/3">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/1a06dfe2-b5fb-39a7-a8d1-c3f1a1018870" />
			      <c:relevance>0.070</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/77d9769c-005e-303e-8aef-6b835e232e6f">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/MusicAlbum" />
			      <c:name>Scream</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/6">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/77d9769c-005e-303e-8aef-6b835e232e6f" />
			      <!--MusicAlbum: Scream; -->
			      <c:detection>[ MTV to fame. Videos such as "Black or White" and ]"Scream"[ made him a staple on MTV in the 1990s. Through]</c:detection>
			      <c:prefix>MTV to fame. Videos such as "Black or White" and</c:prefix>
			      <c:exact>"Scream"</c:exact>
			      <c:suffix>made him a staple on MTV in the 1990s. Through</c:suffix>
			      <c:offset>1021</c:offset>
			      <c:length>8</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/4">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/77d9769c-005e-303e-8aef-6b835e232e6f" />
			      <c:relevance>0.204</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/8e3b7590-7a7f-3f48-bab6-116f7ff3d530">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/IndustryTerm" />
			      <c:name>promotional tool</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/7">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/8e3b7590-7a7f-3f48-bab6-116f7ff3d530" />
			      <!--IndustryTerm: promotional tool; -->
			      <c:detection>[transforming the medium into an art form and a ]promotional tool[, and the popularity of these videos helped to]</c:detection>
			      <c:prefix>transforming the medium into an art form and a</c:prefix>
			      <c:exact>promotional tool</c:exact>
			      <c:suffix>, and the popularity of these videos helped to</c:suffix>
			      <c:offset>865</c:offset>
			      <c:length>16</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/5">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/8e3b7590-7a7f-3f48-bab6-116f7ff3d530" />
			      <c:relevance>0.233</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/67ac230f-02a3-3009-9ddc-371f5fa77694">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <!--Michael Joseph Jackson-->
			      <c:relationsubject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <c:relationobject>a dominant figure</c:relationobject>
			      <c:verb>become</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/8">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/67ac230f-02a3-3009-9ddc-371f5fa77694" />
			      <!--GenericRelations: relationsubject: Michael Joseph Jackson; relationobject: a dominant figure; verb: become; -->
			      <c:detection>[his solo career in 1971.In the early 1980s, ]Jackson became a dominant figure[ in popular music. The music videos for his]</c:detection>
			      <c:prefix>his solo career in 1971.In the early 1980s,</c:prefix>
			      <c:exact>Jackson became a dominant figure</c:exact>
			      <c:suffix>in popular music. The music videos for his</c:suffix>
			      <c:offset>655</c:offset>
			      <c:length>32</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/83784258-3998-31fe-ab68-d95d91ce49f3">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Position" />
			      <c:name>businessman</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/9">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/83784258-3998-31fe-ab68-d95d91ce49f3" />
			      <!--Position: businessman; -->
			      <c:detection>[an American recording artist, entertainer, and ]businessman[. Referred to as the King of Pop, or by his]</c:detection>
			      <c:prefix>an American recording artist, entertainer, and</c:prefix>
			      <c:exact>businessman</c:exact>
			      <c:suffix>. Referred to as the King of Pop, or by his</c:suffix>
			      <c:offset>111</c:offset>
			      <c:length>11</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/6">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/83784258-3998-31fe-ab68-d95d91ce49f3" />
			      <c:relevance>0.318</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/b4380851-0d00-32ef-aabe-58824a49f10c">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Position" />
			      <c:name>recording artist</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/10">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/b4380851-0d00-32ef-aabe-58824a49f10c" />
			      <!--Position: recording artist; -->
			      <c:detection>[29, 1958 ? June 25, 2009) was an American ]recording artist[, entertainer, and businessman. Referred to as]</c:detection>
			      <c:prefix>29, 1958 ? June 25, 2009) was an American</c:prefix>
			      <c:exact>recording artist</c:exact>
			      <c:suffix>, entertainer, and businessman. Referred to as</c:suffix>
			      <c:offset>76</c:offset>
			      <c:length>16</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/11">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/b4380851-0d00-32ef-aabe-58824a49f10c" />
			      <!--Position: recording artist; -->
			      <c:detection>[of awards, which have made him the most-awarded ]recording artist[ in the history of popular]</c:detection>
			      <c:prefix>of awards, which have made him the most-awarded</c:prefix>
			      <c:exact>recording artist</c:exact>
			      <c:suffix>in the history of popular</c:suffix>
			      <c:offset>2244</c:offset>
			      <c:length>16</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/7">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/b4380851-0d00-32ef-aabe-58824a49f10c" />
			      <c:relevance>0.584</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/533855a3-b73a-3e50-be05-d1801e3503d0">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Position" />
			      <c:name>dancer</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/12">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/533855a3-b73a-3e50-be05-d1801e3503d0" />
			      <!--Position: dancer; -->
			      <c:detection>[Hall of Fame as the first (and currently only) ]dancer[ from the world of pop and rock 'n' roll. Some of]</c:detection>
			      <c:prefix>Hall of Fame as the first (and currently only)</c:prefix>
			      <c:exact>dancer</c:exact>
			      <c:suffix>from the world of pop and rock 'n' roll. Some of</c:suffix>
			      <c:offset>1740</c:offset>
			      <c:length>6</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/8">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/533855a3-b73a-3e50-be05-d1801e3503d0" />
			      <c:relevance>0.084</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/6a9d0d43-48db-3369-99b3-2de6e85346f4">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/EntertainmentAwardEvent" />
			      <c:name>the Grammy Lifetime Achievement Award</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/13">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/6a9d0d43-48db-3369-99b3-2de6e85346f4" />
			      <!--EntertainmentAwardEvent: the Grammy Lifetime Achievement Award; -->
			      <c:detection>[Awards (as well as the Grammy Legend Award and ]the Grammy Lifetime Achievement Award[); 26 American Music Awards (more than any other]</c:detection>
			      <c:prefix>Awards (as well as the Grammy Legend Award and</c:prefix>
			      <c:exact>the Grammy Lifetime Achievement Award</c:exact>
			      <c:suffix>); 26 American Music Awards (more than any other</c:suffix>
			      <c:offset>1917</c:offset>
			      <c:length>37</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/9">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/6a9d0d43-48db-3369-99b3-2de6e85346f4" />
			      <c:relevance>0.070</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/208c86bf-a46a-3d06-8843-719fad58805f">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <!--Michael Joseph Jackson-->
			      <c:relationobject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <c:verb>recognize</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/14">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/208c86bf-a46a-3d06-8843-719fad58805f" />
			      <!--GenericRelations: relationobject: Michael Joseph Jackson; verb: recognize; -->
			      <c:detection>[ to as the King of Pop, or by his initials MJ,[2] ]Jackson is recognized as the most successful entertainer of all time by Guinness World Records[. His contribution to music, dance, and fashion,]</c:detection>
			      <c:prefix>to as the King of Pop, or by his initials MJ,[2]</c:prefix>
			      <c:exact>Jackson is recognized as the most successful entertainer of all time by Guinness World Records</c:exact>
			      <c:suffix>. His contribution to music, dance, and fashion,</c:suffix>
			      <c:offset>182</c:offset>
			      <c:length>94</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/5a5e4ce5-5f85-3f1c-ad2d-c816cf155690">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Position" />
			      <c:name>artist</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/15">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/5a5e4ce5-5f85-3f1c-ad2d-c816cf155690" />
			      <!--Position: artist; -->
			      <c:detection>[26 American Music Awards (more than any other ]artist[, including the "Artist of the Century"); 13]</c:detection>
			      <c:prefix>26 American Music Awards (more than any other</c:prefix>
			      <c:exact>artist</c:exact>
			      <c:suffix>, including the "Artist of the Century"); 13</c:suffix>
			      <c:offset>2003</c:offset>
			      <c:length>6</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/16">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/5a5e4ce5-5f85-3f1c-ad2d-c816cf155690" />
			      <!--Position: artist; -->
			      <c:detection>[(more than any other artist, including the "]Artist[ of the Century"); 13 number-one singles in the]</c:detection>
			      <c:prefix>(more than any other artist, including the "</c:prefix>
			      <c:exact>Artist</c:exact>
			      <c:suffix>of the Century"); 13 number-one singles in the</c:suffix>
			      <c:offset>2026</c:offset>
			      <c:length>6</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/10">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/5a5e4ce5-5f85-3f1c-ad2d-c816cf155690" />
			      <c:relevance>0.076</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/e95544dc-bd69-395d-bef4-36b612273b72">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/EntertainmentAwardEvent" />
			      <c:name>26 American Music Awards</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/17">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/e95544dc-bd69-395d-bef4-36b612273b72" />
			      <!--EntertainmentAwardEvent: 26 American Music Awards; -->
			      <c:detection>[and the Grammy Lifetime Achievement Award); ]26 American Music Awards[ (more than any other artist, including the]</c:detection>
			      <c:prefix>and the Grammy Lifetime Achievement Award);</c:prefix>
			      <c:exact>26 American Music Awards</c:exact>
			      <c:suffix>(more than any other artist, including the</c:suffix>
			      <c:offset>1957</c:offset>
			      <c:length>24</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/11">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/e95544dc-bd69-395d-bef4-36b612273b72" />
			      <c:relevance>0.070</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/dabb5001-514c-3868-9131-d5db854614d5">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/EntertainmentAwardEvent" />
			      <c:name>13 Grammy Awards</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/18">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/dabb5001-514c-3868-9131-d5db854614d5" />
			      <!--EntertainmentAwardEvent: 13 Grammy Awards; -->
			      <c:detection>[include multiple Guinness World Records; ]13 Grammy Awards[ (as well as the Grammy Legend Award and the]</c:detection>
			      <c:prefix>include multiple Guinness World Records;</c:prefix>
			      <c:exact>13 Grammy Awards</c:exact>
			      <c:suffix>(as well as the Grammy Legend Award and the</c:suffix>
			      <c:offset>1860</c:offset>
			      <c:length>16</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/12">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/dabb5001-514c-3868-9131-d5db854614d5" />
			      <c:relevance>0.070</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/d54ff17a-4096-32dd-a824-9475960edcf0">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Organization" />
			      <c:name>The Jackson</c:name>
			      <c:organizationtype>N/A</c:organizationtype>
			      <c:nationality>N/A</c:nationality>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/19">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/d54ff17a-4096-32dd-a824-9475960edcf0" />
			      <!--Organization: The Jackson; -->
			      <c:detection>[for over four decades. The seventh child of ]the Jackson[ family, he debuted on the professional music]</c:detection>
			      <c:prefix>for over four decades. The seventh child of</c:prefix>
			      <c:exact>the Jackson</c:exact>
			      <c:suffix>family, he debuted on the professional music</c:suffix>
			      <c:offset>457</c:offset>
			      <c:length>11</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/20">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/d54ff17a-4096-32dd-a824-9475960edcf0" />
			      <!--Organization: The Jackson; -->
			      <c:detection>[scene along with his brothers as a member of ]The Jackson[ 5, then the Jacksons in 1964, and began his solo]</c:detection>
			      <c:prefix>scene along with his brothers as a member of</c:prefix>
			      <c:exact>The Jackson</c:exact>
			      <c:suffix>5, then the Jacksons in 1964, and began his solo</c:suffix>
			      <c:offset>559</c:offset>
			      <c:length>11</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/13">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/d54ff17a-4096-32dd-a824-9475960edcf0" />
			      <c:relevance>0.290</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/comphash-1/8c414df2-355f-318d-9a60-ab13a1db76ab">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Company" />
			      <c:name>MTV</c:name>
			      <c:nationality>N/A</c:nationality>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/21">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/comphash-1/8c414df2-355f-318d-9a60-ab13a1db76ab" />
			      <!--Company: MTV; -->
			      <c:detection>[to bring the relatively new television channel ]MTV[ to fame. Videos such as "Black or White" and]</c:detection>
			      <c:prefix>to bring the relatively new television channel</c:prefix>
			      <c:exact>MTV</c:exact>
			      <c:suffix>to fame. Videos such as "Black or White" and</c:suffix>
			      <c:offset>972</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/22">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/comphash-1/8c414df2-355f-318d-9a60-ab13a1db76ab" />
			      <!--Company: MTV; -->
			      <c:detection>[or White" and "Scream" made him a staple on ]MTV[ in the 1990s. Through stage performances and]</c:detection>
			      <c:prefix>or White" and "Scream" made him a staple on</c:prefix>
			      <c:exact>MTV</c:exact>
			      <c:suffix>in the 1990s. Through stage performances and</c:suffix>
			      <c:offset>1051</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/14">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/comphash-1/8c414df2-355f-318d-9a60-ab13a1db76ab" />
			      <c:relevance>0.257</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/er/geo/country/ralg-geo1/152649df-347e-e289-1a9e-acc883e07d17">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/er/Geo/Country" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <!--United States-->
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/a54ac18b-a956-3b5c-b235-0609ca899305" />
			      <c:name>United States</c:name>
			      <c:latitude>40.4230003233</c:latitude>
			      <c:longitude>-98.7372244786</c:longitude>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/a54ac18b-a956-3b5c-b235-0609ca899305">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Country" />
			      <c:name>United States</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/23">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/a54ac18b-a956-3b5c-b235-0609ca899305" />
			      <!--Country: United States; -->
			      <c:detection>[of the Century"); 13 number-one singles in the ]United States[ in his solo career and the estimated sale of]</c:detection>
			      <c:prefix>of the Century"); 13 number-one singles in the</c:prefix>
			      <c:exact>United States</c:exact>
			      <c:suffix>in his solo career and the estimated sale of</c:suffix>
			      <c:offset>2080</c:offset>
			      <c:length>13</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/15">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/a54ac18b-a956-3b5c-b235-0609ca899305" />
			      <c:relevance>0.070</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/388e8b9b-c502-31a9-bd2b-e02c101779cb">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <c:relationsubject>the popularity of these videos</c:relationsubject>
			      <!--MTV-->
			      <c:relationobject rdf:resource="http://d.opencalais.com/comphash-1/8c414df2-355f-318d-9a60-ab13a1db76ab" />
			      <c:verb>bring</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/24">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/388e8b9b-c502-31a9-bd2b-e02c101779cb" />
			      <!--GenericRelations: relationsubject: the popularity of these videos; relationobject: MTV; verb: bring; -->
			      <c:detection>[into an art form and a promotional tool, and ]the popularity of these videos helped to bring the relatively new television channel MTV[ to fame. Videos such as "Black or White" and]</c:detection>
			      <c:prefix>into an art form and a promotional tool, and</c:prefix>
			      <c:exact>the popularity of these videos helped to bring the relatively new television channel MTV</c:exact>
			      <c:suffix>to fame. Videos such as "Black or White" and</c:suffix>
			      <c:offset>887</c:offset>
			      <c:length>88</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/Person" />
			      <c:name>Michael Joseph Jackson</c:name>
			      <c:persontype>N/A</c:persontype>
			      <c:nationality>N/A</c:nationality>
			      <c:commonname>Michael Joseph Jackson</c:commonname>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/25">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[]Michael Joseph Jackson[[1] (August 29, 1958 ? June 25, 2009) was an]</c:detection>
			      <c:prefix />
			      <c:exact>Michael Joseph Jackson</c:exact>
			      <c:suffix>[1] (August 29, 1958 ? June 25, 2009) was an</c:suffix>
			      <c:offset>0</c:offset>
			      <c:length>22</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/26">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[entertainer, and businessman. Referred to as ]the King[ of Pop, or by his initials MJ,[2] Jackson is]</c:detection>
			      <c:prefix>entertainer, and businessman. Referred to as</c:prefix>
			      <c:exact>the King</c:exact>
			      <c:suffix>of Pop, or by his initials MJ,[2] Jackson is</c:suffix>
			      <c:offset>139</c:offset>
			      <c:length>8</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/27">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[Referred to as the King of Pop, or by ]his[ initials MJ,[2] Jackson is recognized as the]</c:detection>
			      <c:prefix>Referred to as the King of Pop, or by</c:prefix>
			      <c:exact>his</c:exact>
			      <c:suffix>initials MJ,[2] Jackson is recognized as the</c:suffix>
			      <c:offset>162</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/28">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[ to as the King of Pop, or by his initials MJ,[2] ]Jackson[ is recognized as the most successful entertainer]</c:detection>
			      <c:prefix>to as the King of Pop, or by his initials MJ,[2]</c:prefix>
			      <c:exact>Jackson</c:exact>
			      <c:suffix>is recognized as the most successful entertainer</c:suffix>
			      <c:offset>182</c:offset>
			      <c:length>7</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/29">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[of all time by Guinness World Records. ]His[ contribution to music, dance, and fashion, along]</c:detection>
			      <c:prefix>of all time by Guinness World Records.</c:prefix>
			      <c:exact>His</c:exact>
			      <c:suffix>contribution to music, dance, and fashion, along</c:suffix>
			      <c:offset>278</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/30">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[ along with a much-publicized personal life, made ]him[ a global figure in popular culture for over four]</c:detection>
			      <c:prefix>along with a much-publicized personal life, made</c:prefix>
			      <c:exact>him</c:exact>
			      <c:suffix>a global figure in popular culture for over four</c:suffix>
			      <c:offset>374</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/31">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[The seventh child of the Jackson family, ]he[ debuted on the professional music scene along]</c:detection>
			      <c:prefix>The seventh child of the Jackson family,</c:prefix>
			      <c:exact>he</c:exact>
			      <c:suffix>debuted on the professional music scene along</c:suffix>
			      <c:offset>477</c:offset>
			      <c:length>2</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/32">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[on the professional music scene along with ]his[ brothers as a member of The Jackson 5, then the]</c:detection>
			      <c:prefix>on the professional music scene along with</c:prefix>
			      <c:exact>his</c:exact>
			      <c:suffix>brothers as a member of The Jackson 5, then the</c:suffix>
			      <c:offset>531</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/33">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[Jackson 5, then the Jacksons in 1964, and began ]his[ solo career in 1971.In the early 1980s, Jackson]</c:detection>
			      <c:prefix>Jackson 5, then the Jacksons in 1964, and began</c:prefix>
			      <c:exact>his</c:exact>
			      <c:suffix>solo career in 1971.In the early 1980s, Jackson</c:suffix>
			      <c:offset>611</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/34">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[his solo career in 1971.In the early 1980s, ]Jackson[ became a dominant figure in popular music. The]</c:detection>
			      <c:prefix>his solo career in 1971.In the early 1980s,</c:prefix>
			      <c:exact>Jackson</c:exact>
			      <c:suffix>became a dominant figure in popular music. The</c:suffix>
			      <c:offset>655</c:offset>
			      <c:length>7</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/35">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[figure in popular music. The music videos for ]his[ songs, including those of "Beat It", "Billie]</c:detection>
			      <c:prefix>figure in popular music. The music videos for</c:prefix>
			      <c:exact>his</c:exact>
			      <c:suffix>songs, including those of "Beat It", "Billie</c:suffix>
			      <c:offset>727</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/36">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[such as "Black or White" and "Scream" made ]him[ a staple on MTV in the 1990s. Through stage]</c:detection>
			      <c:prefix>such as "Black or White" and "Scream" made</c:prefix>
			      <c:exact>him</c:exact>
			      <c:suffix>a staple on MTV in the 1990s. Through stage</c:suffix>
			      <c:offset>1035</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/37">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[Through stage performances and music videos, ]Jackson[ popularized a number of dance techniques, such]</c:detection>
			      <c:prefix>Through stage performances and music videos,</c:prefix>
			      <c:exact>Jackson</c:exact>
			      <c:suffix>popularized a number of dance techniques, such</c:suffix>
			      <c:offset>1114</c:offset>
			      <c:length>7</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/38">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[such as the robot and the moonwalk, to which ]he[ gave the name. His distinctive musical sound and]</c:detection>
			      <c:prefix>such as the robot and the moonwalk, to which</c:prefix>
			      <c:exact>he</c:exact>
			      <c:suffix>gave the name. His distinctive musical sound and</c:suffix>
			      <c:offset>1209</c:offset>
			      <c:length>2</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/39">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[and the moonwalk, to which he gave the name. ]His[ distinctive musical sound and vocal style have]</c:detection>
			      <c:prefix>and the moonwalk, to which he gave the name.</c:prefix>
			      <c:exact>His</c:exact>
			      <c:suffix>distinctive musical sound and vocal style have</c:suffix>
			      <c:offset>1227</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/40">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[ hip hop, pop, contemporary R&amp;B, and rock artists.]Jackson['s 1982 album Thriller is the best-selling album]</c:detection>
			      <c:prefix>hip hop, pop, contemporary R&amp;B, and rock artists.</c:prefix>
			      <c:exact>Jackson</c:exact>
			      <c:suffix>'s 1982 album Thriller is the best-selling album</c:suffix>
			      <c:offset>1347</c:offset>
			      <c:length>7</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/41">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[Thriller is the best-selling album of all time. ]His[ other records, including Off the Wall (1979),]</c:detection>
			      <c:prefix>Thriller is the best-selling album of all time.</c:prefix>
			      <c:exact>His</c:exact>
			      <c:suffix>other records, including Off the Wall (1979),</c:suffix>
			      <c:offset>1416</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/42">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[also rank among the world's best-selling. ]Jackson[ is one of the few artists to have been inducted]</c:detection>
			      <c:prefix>also rank among the world's best-selling.</c:prefix>
			      <c:exact>Jackson</c:exact>
			      <c:suffix>is one of the few artists to have been inducted</c:suffix>
			      <c:offset>1558</c:offset>
			      <c:length>7</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/43">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[into the Rock and Roll Hall of Fame twice. ]He[ was also inducted into the Dance Hall of Fame as]</c:detection>
			      <c:prefix>into the Rock and Roll Hall of Fame twice.</c:prefix>
			      <c:exact>He</c:exact>
			      <c:suffix>was also inducted into the Dance Hall of Fame as</c:suffix>
			      <c:offset>1657</c:offset>
			      <c:length>2</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/44">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[ from the world of pop and rock 'n' roll. Some of ]his[ other achievements include multiple Guinness]</c:detection>
			      <c:prefix>from the world of pop and rock 'n' roll. Some of</c:prefix>
			      <c:exact>his</c:exact>
			      <c:suffix>other achievements include multiple Guinness</c:suffix>
			      <c:offset>1796</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/45">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[13 number-one singles in the United States in ]his[ solo career and the estimated sale of over 750]</c:detection>
			      <c:prefix>13 number-one singles in the United States in</c:prefix>
			      <c:exact>his</c:exact>
			      <c:suffix>solo career and the estimated sale of over 750</c:suffix>
			      <c:offset>2097</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/46">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[sale of over 750 million records worldwide. ]Jackson[ won hundreds of awards, which have made him the]</c:detection>
			      <c:prefix>sale of over 750 million records worldwide.</c:prefix>
			      <c:exact>Jackson</c:exact>
			      <c:suffix>won hundreds of awards, which have made him the</c:suffix>
			      <c:offset>2175</c:offset>
			      <c:length>7</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/47">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <!--Person: Michael Joseph Jackson; -->
			      <c:detection>[Jackson won hundreds of awards, which have made ]him[ the most-awarded recording artist in the history]</c:detection>
			      <c:prefix>Jackson won hundreds of awards, which have made</c:prefix>
			      <c:exact>him</c:exact>
			      <c:suffix>the most-awarded recording artist in the history</c:suffix>
			      <c:offset>2223</c:offset>
			      <c:length>3</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/16">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <c:relevance>0.857</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/9986c978-0ef5-30dc-8b59-a6dcfdea5a0c">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <!--Michael Joseph Jackson-->
			      <c:relationsubject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <c:relationobject>a number of dance techniques</c:relationobject>
			      <c:verb>popularize</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/48">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/9986c978-0ef5-30dc-8b59-a6dcfdea5a0c" />
			      <!--GenericRelations: relationsubject: Michael Joseph Jackson; relationobject: a number of dance techniques; verb: popularize; -->
			      <c:detection>[Through stage performances and music videos, ]Jackson popularized a number of dance techniques[, such as the robot and the moonwalk, to which he]</c:detection>
			      <c:prefix>Through stage performances and music videos,</c:prefix>
			      <c:exact>Jackson popularized a number of dance techniques</c:exact>
			      <c:suffix>, such as the robot and the moonwalk, to which he</c:suffix>
			      <c:offset>1114</c:offset>
			      <c:length>48</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/59791845-1b02-3df0-92d6-869edf909b9e">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <!--Michael Joseph Jackson-->
			      <c:relationsubject rdf:resource="http://d.opencalais.com/pershash-1/7b2f85f1-316d-3477-b78f-d9246db29e10" />
			      <c:relationobject>hundreds of awards</c:relationobject>
			      <c:verb>win</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/49">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/59791845-1b02-3df0-92d6-869edf909b9e" />
			      <!--GenericRelations: relationsubject: Michael Joseph Jackson; relationobject: hundreds of awards; verb: win; -->
			      <c:detection>[sale of over 750 million records worldwide. ]Jackson won hundreds of awards[, which have made him the most-awarded recording]</c:detection>
			      <c:prefix>sale of over 750 million records worldwide.</c:prefix>
			      <c:exact>Jackson won hundreds of awards</c:exact>
			      <c:suffix>, which have made him the most-awarded recording</c:suffix>
			      <c:offset>2175</c:offset>
			      <c:length>30</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/er/company/ralg-tr1r/54a64f90-a83c-35ef-b2db-b5e2a3287b6c">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/er/Company" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <!--MTV-->
			      <c:subject rdf:resource="http://d.opencalais.com/comphash-1/8c414df2-355f-318d-9a60-ab13a1db76ab" />
			      <c:score>1.0</c:score>
			      <c:name>MTV Oy</c:name>
			      <c:shortname>MTV</c:shortname>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/23ed6e23-649c-3d2f-8cdb-665815aa075e">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/e/MusicAlbum" />
			      <c:name>Thriller</c:name>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/50">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/23ed6e23-649c-3d2f-8cdb-665815aa075e" />
			      <!--MusicAlbum: Thriller; -->
			      <c:detection>[ including those of "Beat It", "Billie Jean", and ]"Thriller"[, were credited with transforming the medium into]</c:detection>
			      <c:prefix>including those of "Beat It", "Billie Jean", and</c:prefix>
			      <c:exact>"Thriller"</c:exact>
			      <c:suffix>, were credited with transforming the medium into</c:suffix>
			      <c:offset>787</c:offset>
			      <c:length>10</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/51">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/23ed6e23-649c-3d2f-8cdb-665815aa075e" />
			      <!--MusicAlbum: Thriller; -->
			      <c:detection>[R&amp;B, and rock artists.Jackson's 1982 album ]Thriller[ is the best-selling album of all time. His other]</c:detection>
			      <c:prefix>R&amp;B, and rock artists.Jackson's 1982 album</c:prefix>
			      <c:exact>Thriller</c:exact>
			      <c:suffix>is the best-selling album of all time. His other</c:suffix>
			      <c:offset>1368</c:offset>
			      <c:length>8</c:length>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Relevance/17">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/RelevanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/23ed6e23-649c-3d2f-8cdb-665815aa075e" />
			      <c:relevance>0.289</c:relevance>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/genericHasher-1/6934b8dd-93a7-342f-8534-70b9847c4c52">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/em/r/GenericRelations" />
			      <!--Thriller-->
			      <c:relationsubject rdf:resource="http://d.opencalais.com/genericHasher-1/23ed6e23-649c-3d2f-8cdb-665815aa075e" />
			      <c:relationobject>the best-selling album of all time</c:relationobject>
			      <c:verb>be</c:verb>
			   </rdf:Description>
			   <rdf:Description rdf:about="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34/Instance/52">
			      <rdf:type rdf:resource="http://s.opencalais.com/1/type/sys/InstanceInfo" />
			      <c:docId rdf:resource="http://d.opencalais.com/dochash-1/3ad31be9-1ef1-3755-92e5-4e9270552a34" />
			      <c:subject rdf:resource="http://d.opencalais.com/genericHasher-1/6934b8dd-93a7-342f-8534-70b9847c4c52" />
			      <!--GenericRelations: relationsubject: Thriller; relationobject: the best-selling album of all time; verb: be; -->
			      <c:detection>[numerous hip hop, pop, contemporary R&amp;B, and ]rock artists.Jackson's 1982 album Thriller is the best-selling album of all time[. His other records, including Off the Wall]</c:detection>
			      <c:prefix>numerous hip hop, pop, contemporary R&amp;B, and</c:prefix>
			      <c:exact>rock artists.Jackson's 1982 album Thriller is the best-selling album of all time</c:exact>
			      <c:suffix>. His other records, including Off the Wall</c:suffix>
			      <c:offset>1334</c:offset>
			      <c:length>80</c:length>
			   </rdf:Description>
			</rdf:RDF>
		*/

	}
}
