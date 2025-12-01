importScripts('decoder-core.js')
var self = this

var isInitialized_ = false
var hasFrame_ = false

var decoderInstancePtr_ = _malloc(4)
if (_WelsCreateDecoder(decoderInstancePtr_) != 0) {
		var errorInfo = 'ERROR: _WelsCreateDecoder failed'
		console.log(errorInfo)
		throw errorInfo
}
var decoderInstance_ = getValue(decoderInstancePtr_, 'i32')
if (_decoderInitialize(decoderInstance_) != 0) {
		var errorInfo = 'ERROR: _decoderInitialize'
		console.log(errorInfo)
		throw errorInfo
}

console.log('Decoder worker: initialized')

var MAX_NALU_SIZE = 1024 * 1024 * 2
var naluBufferPtr_ = _malloc(MAX_NALU_SIZE)
var naluBuffer_ = HEAPU8.subarray(naluBufferPtr_, naluBufferPtr_ + MAX_NALU_SIZE)

var dstDataPtr_ = _malloc(4 * 3) // 3 pointers to YUV outputs
var dstInfoPtr_ = _malloc(48) // in current version "d7b344f" the size of struct is 48 

var rgbaPtr_ = 0

self.onmessage = function (evt) {
	var ret, w, h, strideY, strideUV, executionTime
	
	if (evt.data.payload) {
		var data = new Uint8Array(evt.data.payload)
		naluBuffer_.set(data)
		setValue(dstDataPtr_ + 0, 0, 'i32')
		setValue(dstDataPtr_ + 4, 0, 'i32')
		setValue(dstDataPtr_ + 8, 0, 'i32')
		_memset(dstInfoPtr_, 0, 48)
		ret = _decoderDecodeFrame(decoderInstance_, naluBufferPtr_, data.length, dstDataPtr_, dstInfoPtr_)
		// if there is a frame
		if (getValue(dstInfoPtr_, 'i32') == 1) {
			hasFrame_ = true
			w = getValue(dstInfoPtr_ + 24, 'i32')
			h = getValue(dstInfoPtr_ + 28, 'i32')
			strideY = getValue(dstInfoPtr_ + 36, 'i32')
			strideUV = getValue(dstInfoPtr_ + 40, 'i32')
			if (isInitialized_ === false) {
				isInitialized_ = true
				self.postMessage({status: 'INITIALIZED', width: w, height: h})
			}
		}
	}
	
	var command = evt.data.command
	switch (command) {
		case 'PERF_TEST':
			executionTime = runPerfTest()
			self.postMessage({status: 'PERF_TEST', executionTime: executionTime})
			break
		case 'RECV_RGBA':
			if (hasFrame_) {
				rgbaPtr_ = _malloc(w * h * 4)
				_to_rgba(w, h, getValue(dstDataPtr_ + 0, 'i32'), getValue(dstDataPtr_ + 4, 'i32'), getValue(dstDataPtr_ + 8, 'i32'), strideY, strideUV, rgbaPtr_)
				var rgbaData = HEAPU8.subarray(rgbaPtr_, rgbaPtr_ + w * h * 4)
				// the rgbaData has to be copied from the HEAP
				var rgbaDataCopy = new Uint8Array(rgbaData.length)
				rgbaDataCopy.set(rgbaData)
				// deallocate the RGBA buffer
				// var rgbaArrayBuffer = rgbDataCopy.buffer
				self.postMessage({status: 'RGBA_FRAME', 
													payload: rgbaDataCopy.buffer}, [rgbaDataCopy.buffer])
				_free(rgbaPtr_)
			}
		  break
		case 'RECV_YUV':
			if (hasFrame_) {
				var yuvCopy = new Uint8Array(w * h * 3 / 2)
				var dataPtr = getValue(dstDataPtr_ + 0, 'i32') // Y
				for (var y = 0; y < h; y++) {
					yuvCopy.set(HEAPU8.subarray(dataPtr, dataPtr + w), y * w)
					dataPtr += strideY
				}
				var off = w * h
				var halfW = w / 2
				var halfH = h / 2
				dataPtr = getValue(dstDataPtr_ + 4, 'i32') // U
				for (var y = 0; y < halfH; y++) {
					yuvCopy.set(HEAPU8.subarray(dataPtr, dataPtr + halfW), off + y * halfW)
					dataPtr += strideUV
				}
				off += halfW * halfH
				dataPtr = getValue(dstDataPtr_ + 8, 'i32') // V
				for (var y = 0; y < halfH; y++) {
					yuvCopy.set(HEAPU8.subarray(dataPtr, dataPtr + halfW), off + y * halfW)
					dataPtr += strideUV
				}
				var yuvArrayBuffer = yuvCopy.buffer
				self.postMessage({status: 'YUV_FRAME',
													payload: yuvArrayBuffer}, [yuvArrayBuffer])			
			}
			break
		case 'CLOSE':
			// maybe not neccessary here since the close() method should release the entire running context
			_free(decoderInstancePtr_)
			_free(naluBufferPtr_)
			_free(dstDataPtr_)
			_free(dstInfoPtr_)
			_decoderUninitialize(decoderInstance_)
			_WelsDestroyDecoder(decoderInstance_)
			// terminate this_
			console.log('Decoder worker: closing...')
			self.close()
			
			break
		default:
			break
	}
	
	hasFrame_ = false
}

// performance test
function runTask() {
	var decoderInstancePtr_ = _malloc(4)
	if (_WelsCreateDecoder(decoderInstancePtr_) != 0) {
		var errorInfo = 'ERROR: _WelsCreateDecoder failed'
		console.log(errorInfo)
		throw errorInfo
	}
	var decoderInstance_ = getValue(decoderInstancePtr_, 'i32')
	if (_decoderInitialize(decoderInstance_) != 0) {
		var errorInfo = 'ERROR: _decoderInitialize'
		console.log(errorInfo)
		throw errorInfo
	}

	_free(decoderInstancePtr_)
	_decoderUninitialize(decoderInstance_)
	_WelsDestroyDecoder(decoderInstance_)
}

function runPerfTest() {
	var startTime = performance.now()
	
	for (var i = 0; i < 300; i++) {
		runTask()
	}
	
	var executionTime = performance.now() - startTime
	return executionTime
}