import yaml
import os
import json
import subprocess

configuration = {}
color_none = "\033[0m"
color_green = "\033[0;32m"
color_yellow = "\033[1;33m"
color_red = "\033[0;31m"
color_blue = "\033[0;34m"
color_purple = "\033[0;35m"

def buildCompiler():
    print(' == 1 ==[ ]== Build Your Compiler')
    buildScriptPath = os.path.join(configuration['path']['compiler'], 'build.sh')
    process = None
    try:
        process = subprocess.Popen(["sh", buildScriptPath], cwd=configuration['path']['compiler'], stdout=subprocess.PIPE, shell=False)
        process.wait(configuration['buildlimit'])
        if process.returncode == 0:
            print('{} == 1 ==[√]== Build successfully.{}'.format(color_green, color_none))
        else:
            print('{} == 1 ==[R]== Build failed with exitcode {}.{}'.format(color_purple, process.returncode, color_none))
            exit(0)
        pass
    except subprocess.TimeoutExpired as identifier:
        print('{} == 1 ==[T]== Build Timeout.{}'.format(color_yellow, color_none))
        try:
            process.kill()
        except Exception as identifier:
            pass
        exit(0)
        pass
    except Exception as identifier:
        print('{} == 1 ==[x]== Build failed with runtime error {}.{}'.format(color_red, identifier, color_none))
        exit(0)
    

def runSemantic():
    judgeList = open(os.path.join(configuration['path']['dataset'], 'sema/judgelist.txt'), 'r', encoding='utf-8').readlines()
    semaPath = os.path.join(configuration['path']['dataset'], 'sema')
    judgeList = [i.strip('\n') for i in judgeList]
    semanticPath = os.path.join(configuration['path']['compiler'], 'semantic.sh')
    print(' == 2 ==[ ]== Semantic Judge Start')
    totalNum = len(judgeList)
    acceptedNum = 0
    wrongNum = 0
    acceptedList = []
    wrongList = []
    for case in judgeList:
        # load test case
        caseData = open(os.path.join(semaPath, case), 'r').readlines()
        caseData = [i.strip('\n') for i in caseData]
        metaIdx = (caseData.index('/*'), caseData.index('*/'))
        metaArea = caseData[metaIdx[0] + 1: metaIdx[1]]
        metaArea = [i.split(': ') for i in metaArea]
        metaDict = {i[0]:i[1] for i in metaArea}
        expectedResult = metaDict['Verdict'] == 'Success'
        print(' == 2 ==[ ]==[ ]== Judge:{}.'.format(case), end='\r')
        dataArea = '\n'.join(caseData[metaIdx[1] + 1:])
        process = subprocess.Popen(["sh", semanticPath], cwd=configuration['path']['compiler'], stdout=subprocess.PIPE, encoding='utf-8', shell=False)
        try:
            process.wait(configuration['timelimit'])
            process.communicate(input=dataArea)
            
            if process.returncode == 0 and expectedResult:
                print('{} == 2 ==[ ]==[√]== Accepted:{}.{}'.format(color_green, case, color_none))
                acceptedNum = acceptedNum + 1
            elif process.returncode != 0 and (not expectedResult):
                print('{} == 2 ==[ ]==[√]== Accepted:{}.{}'.format(color_green, case, color_none))
                acceptedNum = acceptedNum + 1
            else:
                print('{} == 2 ==[ ]==[x]== Wrong Answer:{}.{}'.format(color_red, case, color_none))
                wrongNum = wrongNum + 1
            pass
        except subprocess.TimeoutExpired:
            print('{} == 2 ==[ ]==[T]== Time Limit Exceeded:{}.{}'.format(color_yellow, case, color_none))
            wrongNum = wrongNum + 1
            try:
                process.kill()
            except Exception:
                pass
            pass
        except Exception as identifier:
            print('{} == 2 ==[ ]==[R]== Runtime Error:{}, error Message:{}.{}'.format(color_purple, case, identifier, color_none))
            wrongNum = wrongNum + 1
            pass
    if acceptedNum != totalNum:
        print('{} == 2 ==[x] Semantic Stage Summary: Passed: {} / {}, Ratio: {:.2f}%{}'.format(color_red, acceptedNum, totalNum, acceptedNum * 100.0 / totalNum, color_none))
    else:
        print('{} == 2 ==[√] Semantic Stage Summary: Passed: {} / {}, Ratio: {:.2f}% All passed!{}'.format(color_green, acceptedNum, totalNum, acceptedNum * 100.0 / totalNum, color_none))
    pass

def runCodegen():
    pass

def runOptimize():
    pass


if __name__ == '__main__':
    content = open('config.yaml', 'r', encoding='utf-8').read()
    configuration = yaml.safe_load(content)
    assert 'stage' in configuration.keys()
    assert 'path' in configuration.keys()
    assert 'compiler' in configuration['path'].keys()
    assert 'dataset' in configuration['path'].keys()
    assert 'timelimit' in configuration.keys()
    assert 'memlimit' in configuration.keys()
    assert 'instlimit' in configuration.keys()
    assert 'buildlimit' in configuration.keys()
    buildCompiler()
    if configuration['stage'] == 'semantic':
        runSemantic()
    elif configuration['stage'] == 'codegen':
        runCodegen()
    elif configuration['stage'] == 'optimize':
        runOptimize()
    else:
        print(' [!] Error: stage can only be [semantic, codegen, optimize]')
    print(' == Judge Finished')
    
    


