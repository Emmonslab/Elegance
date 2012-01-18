/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- change the DB1 to your new database name if you want create a new database
--

CREATE DATABASE IF NOT EXISTS DB1;
USE DB1;

--
-- Table structure for table `backup`
--

CREATE TABLE IF NOT EXISTS `backup` (
  `idx` int(11) NOT NULL auto_increment,
  `filename` varchar(100) default NULL,
  `dbname` varchar(20) default NULL,
  `username` varchar(40) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `colorcodes`
--

CREATE TABLE IF NOT EXISTS `colorcodes` (
  `idx` int(11) NOT NULL auto_increment,
  `neuron` varchar(10) default NULL,
  `color` varchar(10) default NULL,
  `worm` varchar(10) default NULL,
  `series` varchar(10) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `combineflag`
--

CREATE TABLE IF NOT EXISTS `combineflag` (
  `idx` int(11) NOT NULL auto_increment,
  `synid1` int(11) default NULL,
  `syn1` varchar(100) default NULL,
  `synid2` int(11) default NULL,
  `syn2` varchar(100) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `contin`
--

CREATE TABLE IF NOT EXISTS `contin`  (
  `continID` int(10) unsigned NOT NULL auto_increment,
  `sectionNum1` int(11) NOT NULL default '0',
  `sectionNum2` int(11) NOT NULL default '0',
  `CON_Number` int(11) unsigned NOT NULL default '0',
  `CON_AlternateName` varchar(250) default NULL,
  `CON_AlternateName2` varchar(20) default NULL,
  `CON_Remarks` varchar(254) default NULL,
  `type` varchar(20) default 'neuron',
  `series` varchar(30) default NULL,
  `count` int(11) default '0',
  PRIMARY KEY  (`continID`),
  UNIQUE KEY `con_conNum` (`CON_Number`),
  KEY `con_conName` (`CON_AlternateName`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `continmap`
--

CREATE TABLE IF NOT EXISTS`continmap` (
  `idx` int(11) NOT NULL auto_increment,
  `continName` varchar(30) default NULL,
  `continNum` int(11) default NULL,
  `series` varchar(30) default NULL,
  `direction` varchar(30) default NULL,
  `zoom` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `contintable`
--

CREATE TABLE IF NOT EXISTS`contintable` (
  `continName` varchar(20) default NULL,
  `continNum` int(11) default NULL,
  `calculated` set('yes','no') default NULL,
  `printed` set('yes','no') default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `cumulativelist`
--

CREATE TABLE IF NOT EXISTS `cumulativelist`  (
  `idx` int(11) NOT NULL auto_increment,
  `neuron` varchar(10) default NULL,
  `partner` varchar(10) default NULL,
  `type` varchar(10) default NULL,
  `length` tinyint(4) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `degree`
--

CREATE TABLE IF NOT EXISTS `degree` (
  `neuron` varchar(20) default NULL,
  `DegreeE` int(11) default NULL,
  `DegreeIn` int(11) default NULL,
  `DegreeOut` int(11) default NULL,
  `SectionE` int(11) default NULL,
  `SectionIn` int(11) default NULL,
  `SectionOut` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



--
-- Table structure for table `display`
--


CREATE TABLE IF NOT EXISTS `display`  (
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `z` int(11) default NULL,
  `cellbody` tinyint(4) default NULL,
  `remarks` varchar(40) default NULL,
  `continNum` int(11) default NULL,
  `segmentNum` smallint(6) default NULL,
  `idx` int(11) NOT NULL auto_increment,
  `branch` tinyint(4) default '0',
  `objName` varchar(20) default NULL,
  `series` varchar(30) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `display2`
--


CREATE TABLE IF NOT EXISTS `display2`  (
  `x1` int(11) default NULL,
  `y1` int(11) default NULL,
  `z1` int(11) default NULL,
  `cellbody1` tinyint(4) default NULL,
  `remarks1` varchar(40) default NULL,
  `continNum` int(11) default NULL,
  `segmentNum` smallint(6) default NULL,
  `idx` int(11) NOT NULL auto_increment,
  `branch1` tinyint(4) default '0',
  `objName1` varchar(20) default NULL,
  `series1` varchar(30) default NULL,
  `x2` int(11) default NULL,
  `y2` int(11) default NULL,
  `z2` int(11) default NULL,
  `cellbody2` tinyint(4) default NULL,
  `remarks2` varchar(40) default NULL,
  `branch2` tinyint(4) default '0',
  `objName2` varchar(20) default NULL,
  `series2` varchar(30) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `edge`
--

CREATE TABLE IF NOT EXISTS`edge` (
  `name` varchar(20) default NULL,
  `abbrname` varchar(20) default NULL,
  `defini` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `etest`
--

CREATE TABLE IF NOT EXISTS `etest` (
  `idx` int(11) NOT NULL auto_increment,
  `x` int(11) default '0',
  `y` int(11) default '0',
  `IMG_Number` varchar(30) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `image`
--

CREATE TABLE IF NOT EXISTS `image`  (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `IMG_Number` varchar(30) NOT NULL default '',
  `IMG_File` varchar(30) NOT NULL default '',
  `IMG_Directory` varchar(50) NOT NULL default '',
  `IMG_Worm` varchar(254) NOT NULL default '',
  `IMG_Series` varchar(30) NOT NULL default '',
  `IMG_PrintNumber` varchar(30) NOT NULL default '',
  `IMG_NegativeNumber` varchar(30) NOT NULL default '',
  `IMG_SectionNumber` int NOT NULL default 0,
  `IMG_EnteredBy` varchar(50) NOT NULL default '',
  `IMG_DateEntered` date NOT NULL default '0000-00-00',
  `IMG_zoomValue` double NOT NULL default '1',
  `IMG_brightnessValue` int(11) NOT NULL default '0',
  `IMG_contrastValue` int(11) NOT NULL default '0',
  `IMG_rotatedValue` double NOT NULL default '0',
  `IMG_Remarks` varchar(254) default NULL,
  `IMG_originX` int(5) NOT NULL default '0',
  `IMG_originY` int(5) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `IMG_IMGNumber` (`IMG_Number`),
  UNIQUE KEY `IMG_IMGFile` (`IMG_Directory`,`IMG_File`),
  KEY `IMG_IMGSection` (`IMG_SectionNumber`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `imagesize`
--

CREATE TABLE IF NOT EXISTS `imagesize` (
  `idx` int(11) NOT NULL auto_increment,
  `IMG_Number` varchar(30) default NULL,
  `width` int(11) default '0',
  `height` int(11) default '0',
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `list`
--

CREATE TABLE IF NOT EXISTS `list` (
  `Neuron` varchar(25) default NULL,
  `Type` varchar(50) default NULL,
  `PresentInHermaphrodite` tinyint(1) default NULL,
  `LocationOfCellBody` varchar(50) default NULL,
  `ProcessInPosterior` tinyint(1) default NULL,
  `Reconstructed` tinyint(1) default NULL,
  `memo` mediumtext
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


--
-- Table structure for table `lockcontin`
--


CREATE TABLE IF NOT EXISTS `lockcontin`  (
  `idx` int(11) NOT NULL auto_increment,
  `continNum` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `matrix`
--

CREATE TABLE IF NOT EXISTS `matrix`  (
  `name` varchar(40) default NULL,
  `idx` int(11) NOT NULL auto_increment,
  `type` varchar(30) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `multiple`
--


CREATE TABLE IF NOT EXISTS `multiple`  (
  `OBJ_Name` int(11) default NULL,
  `fromObj` int(11) default '0',
  `toObj1` int(11) default '0',
  `toObj2` int(11) default '0',
  `toObj3` int(11) default '0',
  `toObj4` int(11) default '0',
  `toObj5` int(11) default '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `nattr`
--

CREATE TABLE IF NOT EXISTS `nattr` (
  `name` varchar(20) default NULL,
  `title` varchar(20) default NULL,
  `subtitle` varchar(20) default NULL,
  `attr_value` tinyint(4) default NULL,
  `display` varchar(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `nbdata`
--

CREATE TABLE IF NOT EXISTS `nbdata` (
  `idx` int(11) NOT NULL auto_increment,
  `n1` varchar(20) default NULL,
  `n2` varchar(20) default NULL,
  `type` varchar(10) default NULL,
  `section` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `neighborhood`
--

CREATE TABLE IF NOT EXISTS `neighborhood` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `OBJ_Name` int(11) DEFAULT NULL,
  `neuron` varchar(20) DEFAULT NULL,
  `neighbors` varchar(100) DEFAULT NULL,
  `neighborNum` int(11) DEFAULT NULL,
  `imgNum` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17324 ;


--
-- Table structure for table `network`
--

CREATE TABLE IF NOT EXISTS `network` (
  `pre` varchar(10) default NULL,
  `post` varchar(10) default NULL,
  `weight` int(11) default NULL,
  `idx` int(11) NOT NULL auto_increment,
  `pp` varchar(30) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

 
--
-- Table structure for table `network2`
--

CREATE TABLE IF NOT EXISTS `network2` (
  `pre` int(11) default NULL,
  `post` int(11) default NULL,
  `weight` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `network3`
--

CREATE TABLE IF NOT EXISTS  `network3` (
  `n1` varchar(40) default NULL,
  `n2` varchar(40) default NULL,
  `direction` tinyint(4) default NULL,
  `method` varchar(10) default NULL,
  `weight` double default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `neurontable`
--

CREATE TABLE IF NOT EXISTS  `neurontable` (
  `name` varchar(40) default NULL,
  `continNum` varchar(40) default NULL,
  `type` varchar(40) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `obj2`
--


CREATE TABLE IF NOT EXISTS `obj2`  (
  `name` int(11) NOT NULL default '0',
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `directory` varchar(100) default NULL,
  `filename` varchar(40) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `obj3`
--


CREATE TABLE IF NOT EXISTS `obj3`  (
  `name` int(11) default NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `directory` varchar(50) default NULL,
  `filename` varchar(40) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `object`
--


CREATE TABLE IF NOT EXISTS `object`  (
  `OBJ_Name` int(11) unsigned NOT NULL auto_increment,
  `OBJ_X` int(10) unsigned NOT NULL default '0',
  `OBJ_Y` int(10) unsigned NOT NULL default '0',
  `OBJ_Remarks` varchar(254) default NULL,
  `IMG_Number` varchar(30) NOT NULL default '',
  `CON_Number` int(11) unsigned NOT NULL default '0',
  `type` set('cell','cell branch point','chemical','electrical','neighborhood') default 'cell',
  `fromObj` varchar(20) default '-1',
  `toObj` varchar(60) default '',
  `checked` tinyint(4) default '0',
  `username` varchar(10) default NULL,
  `DateEntered` date default '0000-00-00',
  `certainty` set('certain','uncertain') default 'certain',
  `size` set('small','normal','large') default 'normal',
  `cellType` tinyint(4) default '0',
  `forMap` varchar(30) default '',
  PRIMARY KEY  (`OBJ_Name`),
  UNIQUE KEY `xyz` (`IMG_Number`,`OBJ_X`,`OBJ_Y`),
  UNIQUE KEY `obj_name_con` (`OBJ_Name`,`CON_Number`),
  UNIQUE KEY `obj_name_img` (`OBJ_Name`,`IMG_Number`),
  KEY `obj_con` (`CON_Number`),
  KEY `obj_type` (`type`),
  KEY `OBJ_From` (`fromObj`),
  KEY `OBJ_To` (`toObj`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `object2`
--


CREATE TABLE IF NOT EXISTS `object2`  (
  `idx` int(11) unsigned NOT NULL auto_increment,
  `OBJ_Name` int(11) unsigned NOT NULL,
  `type` set('cell','cell branch point','chemical','electrical') default 'cell',
  `fromObj` varchar(20) default '-1',
  `toObj` varchar(40) default '-1',
  `username` varchar(10) default NULL,
  `DateEntered` date default '0000-00-00',
  `size` set('small','normal','large') default 'normal',
  `certainty` set('certain','uncertain') default 'certain',
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `records`
--


CREATE TABLE IF NOT EXISTS `records`  (
  `synid` int(11) default NULL,
  `IMG_Number` varchar(20) default NULL,
  `partner1` varchar(50) default NULL,
  `partner2` varchar(50) default NULL,
  `type` varchar(20) default 'chemical'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `records2`
--

CREATE TABLE IF NOT EXISTS `records2`  (
  `synid` int(11) default NULL,
  `IMG_Number` varchar(20) default NULL,
  `partner1` varchar(50) default NULL,
  `partner2` varchar(50) default NULL,
  `type` varchar(20) default 'chemical'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `relationship`
--

CREATE TABLE IF NOT EXISTS `relationship`  (
  `relID` int(10) unsigned NOT NULL auto_increment,
  `REL_Remarks` varchar(254) default NULL,
  `ObjName1` int(11) unsigned NOT NULL default '0',
  `ObjName2` int(11) unsigned NOT NULL default '0',
  `segmentNum` int(11) default '0',
  `continNum` int(11) default NULL,
  PRIMARY KEY  (`relID`),
  UNIQUE KEY `obj1_obj2` (`ObjName1`,`ObjName2`),
  KEY `segmentNum` (`segmentNum`),
  KEY `rel_obj1` (`ObjName1`),
  KEY `rel_obj2` (`ObjName2`),
  KEY `continNum` (`continNum`),
  KEY `segmentNum_2` (`segmentNum`,`continNum`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `report`
--


CREATE TABLE IF NOT EXISTS `report`  (
  `idx` int(11) NOT NULL auto_increment,
  `preCell` varchar(10) default NULL,
  `postCell1` varchar(10) default NULL,
  `postCell2` varchar(10) default NULL,
  `postCell3` varchar(10) default NULL,
  `postCell4` varchar(10) default NULL,
  `type` varchar(30) default NULL,
  `sectionNumber` int(11) default NULL,
  `length` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `reportlist`
--


CREATE TABLE IF NOT EXISTS `reportlist`  (
  `idx` int(11) NOT NULL auto_increment,
  `neuron` varchar(20) default NULL,
  `partner` varchar(20) default NULL,
  `type` varchar(5) default NULL,
  `length` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `reportlist2`
--


CREATE TABLE IF NOT EXISTS `reportlist2`  (
  `idx` int(11) NOT NULL auto_increment,
  `neuron` varchar(20) default NULL,
  `partner` varchar(20) default NULL,
  `type` varchar(5) default NULL,
  `length` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `series`
--


CREATE TABLE IF NOT EXISTS `series`  (
  `idx` int(11) NOT NULL auto_increment,
  `series` varchar(20) default NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `z` int(11) default NULL,
  `zoomx` float(10,4) default NULL,
  `zoomy` float(10,4) default NULL,
  `zoomz` float(10,4) default '1.0000',
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `syn`
--


CREATE TABLE IF NOT EXISTS `syn`  (
  `name` varchar(20) default NULL,
  `syn` varchar(20) default '',
  `syntype` varchar(20) default 'neuron name',
  `priority` tinyint(4) default '2'
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `synapse`
--


CREATE TABLE IF NOT EXISTS `synapse`  (
  `idx` int(11) NOT NULL auto_increment,
  `continNum` int(11) default NULL,
  `foreignContinName` varchar(50) default NULL,
  `type` varchar(10) default NULL,
  `lineNum` int(11) default NULL,
  `fromSec` int(11) default NULL,
  `toSec` int(11) default NULL,
  `synName` int(11) default NULL,
  `objX` int(11) default NULL,
  `objY` int(11) default NULL,
  `comp` varchar(100) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `synapse2`
--


CREATE TABLE IF NOT EXISTS `synapse2`  (
  `idx` int(11) NOT NULL auto_increment,
  `continName` varchar(10) NOT NULL default '',
  `foreignContinName` varchar(50) NOT NULL default '',
  `type` varchar(10) default NULL,
  `lineNum` int(11) default NULL,
  `fromSec` int(11) default NULL,
  `toSec` int(11) default NULL,
  `synName` int(11) default NULL,
  `objX` int(11) default NULL,
  `objY` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `synapsebycontin`
--


CREATE TABLE IF NOT EXISTS `synapsebycontin`  (
  `idx` int(11) NOT NULL auto_increment,
  `continNum` int(11) default NULL,
  `name` varchar(80) default NULL,
  `type` varchar(10) default NULL,
  `objName` int(11) default NULL,
  `sections` tinyint(4) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `synapsecombined`
--


CREATE TABLE IF NOT EXISTS `synapsecombined`  (
  `idx` int(11) NOT NULL auto_increment,
  `pre` varchar(30) default NULL,
  `post` varchar(100) default NULL,
  `type` varchar(10) default NULL,
  `members` text,
  `sections` tinyint(4) default NULL,
  `post1` varchar(40) default NULL,
  `post2` varchar(40) default NULL,
  `post3` varchar(40) default NULL,
  `post4` varchar(40) default NULL,
  `type2` varchar(20) default NULL,
  `series` varchar(20) default NULL,
  `partnerNum` tinyint(4) default '1',
  `mid` varchar(20) default NULL,
  `preobj` varchar(20) default NULL,
  `postobj1` varchar(20) default NULL,
  `postobj2` varchar(20) default NULL,
  `postobj3` varchar(20) default NULL,
  `postobj4` varchar(20) default NULL,
  `continNum` smallint(6) default '0',
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=272 DEFAULT CHARSET=latin1;

--
-- Table structure for table `synapsenomultiple`
--


CREATE TABLE IF NOT EXISTS `synapsenomultiple`  (
  `idx` int(11) NOT NULL auto_increment,
  `pre` varchar(30) default NULL,
  `post` varchar(30) default NULL,
  `sections` smallint(6) default NULL,
  `type` varchar(10) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `synrecord`
--

CREATE TABLE IF NOT EXISTS `synrecord`  (
  `idx` int(11) NOT NULL auto_increment,
  `synID` int(11) default NULL,
  `username` varchar(10) default NULL,
  `DateEntered` date default NULL,
  `certainty` set('certain','uncertain') default 'certain',
  `size` set('small','normal','large') default 'normal',
  `partner` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `tempobj`
--


CREATE TABLE IF NOT EXISTS `tempobj`   (
  `idx` int(11) NOT NULL auto_increment,
  `objName` int(11) NOT NULL default '0',
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `z` int(11) default NULL,
  `type` varchar(20) default NULL,
  `segmentNum` tinyint(4) default NULL,
  `continNum` int(11) default NULL,
  `cellbody` int(11) default '0',
  `remarks` varchar(254) default NULL,
  `continName` varchar(40) default '',
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

--
-- Table structure for table `tempsyn`
--

CREATE TABLE IF NOT EXISTS `tempsyn`  (
  `idx` int(11) NOT NULL auto_increment,
  `continNum` int(11) default NULL,
  `foreignContinName` varchar(50) default NULL,
  `type` varchar(10) default NULL,
  `lineNum` int(11) default NULL,
  `comp` varchar(100) default '',
  `fromSec` int(11) default NULL,
  `toSec` int(11) default NULL,
  `synName` int(11) default NULL,
  `objX` int(11) default NULL,
  `objY` int(11) default NULL,
  PRIMARY KEY  (`idx`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

