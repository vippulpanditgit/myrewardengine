package com.myreward.engine.metamodel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class PackageMetaModel extends BaseMetaModel {
	public String packageName;
	public List<BaseMetaModel> packageMetaModelList = new ArrayList<BaseMetaModel>();
	@Override
	public String[] build() {
		List<String> packageOpcodeList = new ArrayList<String>();
		ListIterator<BaseMetaModel> packageMetaModelListIterator = packageMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageOpcodeList.addAll(Arrays.asList(packageMetaModelListIterator.next().build()));
		}
		return packageOpcodeList.toArray(new String[0]);
	}

}
