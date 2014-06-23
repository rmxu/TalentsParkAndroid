// Jericho HTML Parser - Java based library for analysing and manipulating HTML
// Version 2.6
// Copyright (C) 2007 Martin Jericho
// http://jerichohtml.sourceforge.net/
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of either one of the following licences:
//
// 1. The Eclipse Public License (EPL) version 1.0,
// included in this distribution in the file licence-epl-1.0.html
// or available at http://www.eclipse.org/legal/epl-v10.html
//
// 2. The GNU Lesser General Public License (LGPL) version 2.1 or later,
// included in this distribution in the file licence-lgpl-2.1.txt
// or available at http://www.gnu.org/licenses/lgpl.txt
//
// This library is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the individual licence texts for more details.

package au.id.jericho.lib.html;

final class StartTagTypeMasonNamedBlock extends StartTagTypeGenericImplementation {
	protected static final StartTagTypeMasonNamedBlock INSTANCE=new StartTagTypeMasonNamedBlock();

	private StartTagTypeMasonNamedBlock() {
		super("mason named block","<%",">",EndTagTypeMasonNamedBlock.INSTANCE,true,false,true);
	}

	protected Tag constructTagAt(final Source source, final int pos) {
		final Tag tag=super.constructTagAt(source,pos);
		if (tag==null) return null;
		// A mason named block does not have a '%' before its closing '>' delimiter and requires a matching end tag.
		if (source.charAt(tag.getEnd()-2)=='%') return null; // this is a common server tag, not a named block
		if (source.findNextEndTag(tag.getEnd(),tag.getName(),getCorrespondingEndTagType())==null) return null;
		return tag;
	}
}