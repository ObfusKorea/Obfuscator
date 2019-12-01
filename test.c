int max(int a, int b, int c){
	int ret = 0;
	if(a >= b){
		if(a >= c){
			ret = a;
		}else{
			ret = c;
		}
	}else{
		if(b >= c){
			ret = b;
		}else{
			ret = c;
		}
	}
	return ret;
}

void main () {
	_print(max(3, 4, 5));
}

