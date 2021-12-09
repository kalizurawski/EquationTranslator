import numpy as np
from datetime import datetime

def convertImg(bmp):
    current_time = datetime.now().strftime('%Y-%m-%d_%H:%M:%S')
    filename = current_time + ".csv"
    data = np.asarray(bmp)
    data = 255 - data
    print(data.shape)
    np.savetxt(filename, data, delimiter=",")
    return filename